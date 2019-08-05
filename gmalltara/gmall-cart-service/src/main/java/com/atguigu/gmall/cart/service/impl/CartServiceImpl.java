package com.atguigu.gmall.cart.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.CartInfo;
import com.atguigu.gmall.cart.mapper.CartInfoMapper;
import com.atguigu.gmall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartInfoMapper cartInfoMapper;

    @Autowired
    RedisTemplate<String,String>  redisTemplate;

    //从数据库判断是否是新车
    @Override
    public CartInfo ifCartExists(CartInfo cartInfo) {
        Map<String,String> map=new HashMap<>();
        map.put("skuId",cartInfo.getSkuId());
        map.put("userId",cartInfo.getUserId());
        return cartInfoMapper.selectCartInfo(map);
    }

    //修改数据库的价格，数量
    @Override
    public void updateCart(CartInfo cartInfoDb) {
      cartInfoMapper.updateCart(cartInfoDb);
    }

    //插入新车
    @Override
    public void save(CartInfo cartInfoDb) {
        cartInfoMapper.insert(cartInfoDb);
    }

    //缓存用户的所有购物车
    @Override
    public void syncCache(String userId) {
        //查询数据库中的所有购物车
        List<CartInfo>select=cartInfoMapper.selectByUserId(userId);
        String key="carts:"+userId+":info";
//        if(CollectionUtils.isEmpty(select)){
//            redisTemplate.delete(key);
//        }
        //存放map键值对
        Map<String,String>map=new HashMap<>();
        for(CartInfo info:select){
            map.put(info.getSkuId(),JSON.toJSONString(info));
        }
        redisTemplate.opsForHash().putAll(key,map);
        //redisTemplate.opsForValue().set(key,JSON.toJSONString(map),60*60*2,TimeUnit.SECONDS);

    }

    //从缓存中那用户的购物车
    @Override
    public List<CartInfo> getCartCache(String userId) {
        List<CartInfo> cartInfos=new ArrayList<>();
        String key="carts:"+userId+":info";
        List<Object> values = redisTemplate.opsForHash().values(key);
        //[{"cartPrice":6666.00,"id":"7","isChecked":"1","skuId":"7","skuName":"小米003","skuNum":2,
        // "skuPrice":3333.00,"userId":"1"}, {"cartPrice":2222.00,"id":"9","isChecked":"1","skuId":"6",
        // "skuName":"小米002","skuNum":1,"skuPrice":2222.00,"userId":"1"}, {"cartPrice":2222.00,"id":"8",
        // "isChecked":"1","skuId":"4","skuName":"小米手机mini版本","skuNum":1,"skuPrice":2222.00,"userId":"1"}]
        for(Object o:values){
            System.out.println(o.getClass());//java.lang.String
            //cartInfos.add((CartInfo) o);   //强转  java.lang.ClassCastException: java.lang.String cannot be cast to CartInfo
            CartInfo cartInfo = JSON.parseObject(o.toString(), CartInfo.class);
            cartInfos.add(cartInfo);
        }
        return cartInfos;
    }

    //用户登录， 点击小框框改数据库  redis在自查一次
    @Override
    public void updateCartChecked(CartInfo cartInfo) {
        cartInfoMapper.update(cartInfo);
        syncCache(cartInfo.getUserId());
    }

    //去结算页面是获取选中的购物车
    @Override
    public List<CartInfo> getCartCacheByChecked(String userId) {
        List<CartInfo> cartInfosChecked=new ArrayList<>();
        List<CartInfo> cartInfos = getCartCache(userId); //缓存中获取所有的购物车
        for(CartInfo info:cartInfos){
            if(info.getIsChecked().equals("1")){
                cartInfosChecked.add(info);
            }
        }
        return cartInfosChecked;
    }

    //交易成功，删除交易的商品，同比缓存
    @Override
    public void deleteCartById(List<CartInfo> cartInfos) {
        //批量删除
        cartInfoMapper.deleteByBatch(cartInfos);
        syncCache(cartInfos.get(0).getUserId());
    }

    //cookie中的购物车加到DB，刷新缓存
    @Override
    public void combineCart(List<CartInfo> cartInfos, String userId) {
        for(CartInfo info:cartInfos){
            info.setUserId(userId);
            CartInfo cartInfo = ifCartExists(info);
            if(cartInfo!=null){
                //修改
                cartInfo.setSkuNum(cartInfo.getSkuNum()+info.getSkuNum());
                cartInfo.setCartPrice(cartInfo.getSkuPrice().multiply(new BigDecimal(cartInfo.getSkuNum())));
                cartInfoMapper.updateCart(cartInfo);
            }
            else{
                //新增
                cartInfoMapper.insert(info);
            }
        }
        syncCache(userId);
    }
}
