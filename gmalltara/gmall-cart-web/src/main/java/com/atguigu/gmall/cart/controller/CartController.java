package com.atguigu.gmall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.CartInfo;
import com.atguigu.gmall.bean.SkuInfo;
import com.atguigu.gmall.annotation.LoginRequire;
import com.atguigu.gmall.util.CookieUtil;
import com.atguigu.gmall.service.CartService;
import com.atguigu.gmall.service.SkuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class CartController {

    @Reference
    SkuService skuService;

    @Reference
    CartService cartService;

    @LoginRequire(isNeedSuccess = false)
    @RequestMapping("addToCart")
    public String addToCart(HttpServletRequest request, HttpServletResponse response,  CartInfo cartInfo){
        //封装改购物车
        String skuId=cartInfo.getSkuId();
        SkuInfo skuInfo=skuService.getSkuInfoBySkuId(skuId);
        cartInfo.setIsChecked("1");
        cartInfo.setSkuName(skuInfo.getSkuName());
        cartInfo.setSkuPrice(skuInfo.getPrice());
        cartInfo.setCartPrice(cartInfo.getSkuPrice().multiply(new BigDecimal(cartInfo.getSkuNum())));
        //购物车集合
        List<CartInfo> cartInfos=new ArrayList<>();
        //判断用户是否登录    cookie  还是mysql
        String userId=(String)request.getAttribute("userId");
        if(StringUtils.isBlank(userId)){
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if(StringUtils.isBlank(cartListCookie)){
                //还没有车，直接添加
                cartInfos.add(cartInfo);
            }
            else{
                //判断cookie中是否有相同的车，进行合并
                cartInfos = JSON.parseArray(cartListCookie, CartInfo.class);
                boolean b=ifNewCart(cartInfos,cartInfo);
                if(b){
                    //不是新车，直接添加
                    cartInfos.add(cartInfo);
                }
                else{
                    for(CartInfo info:cartInfos){
                        if(info.getSkuId().equals(cartInfo.getSkuId())){//同一件商品
                            info.setSkuNum(info.getSkuNum()+cartInfo.getSkuNum());
                            info.setCartPrice(info.getSkuPrice().multiply(new BigDecimal(info.getSkuNum())));
                        }
                    }
                }
            }
            System.out.println("cookie"+cartInfos);
            CookieUtil.setCookie(request,response,"cartListCookie", JSON.toJSONString(cartInfos),60*60*24*7,true);
        }

        //用户登录了，存mysql,更新redis
        else{
            cartInfo.setUserId(userId);
            CartInfo cartInfoDb=cartService.ifCartExists(cartInfo);
            if(cartInfoDb!=null){   //需要覆盖
                cartInfoDb.setSkuNum(cartInfo.getSkuNum()+cartInfoDb.getSkuNum());
                cartInfoDb.setCartPrice(cartInfoDb.getSkuPrice().multiply(new BigDecimal(cartInfoDb.getSkuNum())));
                cartService.updateCart(cartInfoDb);
            }
            else{      //直接插入
                cartService.save(cartInfo);
            }
            cartService.syncCache(userId);

        }


        return "redirect:/Cartsuccess";

    }

    @LoginRequire(isNeedSuccess = false)
    @RequestMapping("Cartsuccess")
    public String Cartsuccess(){
        return "success";
    }

    private boolean ifNewCart(List<CartInfo> cartInfos, CartInfo cartInfo) {
        boolean b=true;
        for(CartInfo info:cartInfos){
            if(info.getSkuId().equals(cartInfo.getSkuId())){
                b=false;
            }
        }
        return b;
    }


    //去购物车列表然后结算
    @LoginRequire(isNeedSuccess = false)
    @RequestMapping("cartList")
    public String cartList(HttpServletRequest request,Map map){
        //展示加入购物车的商品

        //用户没登录  取cookie
        String userId=(String)request.getAttribute("userId");
        List<CartInfo>cartInfos=new ArrayList<>();
        if(StringUtils.isBlank(userId)){
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            System.out.println("cookie列表展示"+cartListCookie);
            if(StringUtils.isNotBlank(cartListCookie)){
               cartInfos = JSON.parseArray(cartListCookie, CartInfo.class);
            }
        }
        else{
            //从缓存中取购物车
            cartInfos=cartService.getCartCache(userId);
        }

        map.put("cartList",cartInfos);
        //算总价
        BigDecimal bigDecimal=getTotaoPrice(cartInfos);
        map.put("totalPrice",bigDecimal);//算总价
        return "cartList";
    }

    private BigDecimal getTotaoPrice(List<CartInfo> cartInfos) {
        BigDecimal b=new BigDecimal("0");
        for(CartInfo info:cartInfos){
            if(info.getIsChecked().equals("1")){
                b=b.add(info.getCartPrice());
            }
        }
        return b;
    }

    //更改小框框
    @LoginRequire(isNeedSuccess = false)
    @RequestMapping("checkCart")
    public String checkCart(HttpServletRequest request,HttpServletResponse response,CartInfo cartInfo,Map map){
        List<CartInfo> cartInfos=new ArrayList<>();
        String userId=(String)request.getAttribute("userId");
        if(StringUtils.isBlank(userId)){
            //修改cookie
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if(StringUtils.isNotBlank(cartListCookie)){
                cartInfos= JSON.parseArray(cartListCookie, CartInfo.class);
                for(CartInfo info:cartInfos){
                    if(info.getSkuId().equals(cartInfo.getSkuId())){
                        info.setIsChecked(cartInfo.getIsChecked());
                    }
                }
            }
            System.out.println("更改小框卡箍"+cartInfos);
            CookieUtil.setCookie(request,response,"cartListCookie",JSON.toJSONString(cartInfos),60*60*24*7,true);

        }
        //更改db,redis在自查一次
        else{
            cartInfo.setUserId(userId);
            cartService.updateCartChecked(cartInfo);
            //缓存再查一次
            cartInfos = cartService.getCartCache(userId);
        }
        map.put("cartList",cartInfos);
        map.put("totalPrice",getTotaoPrice(cartInfos));
        return "cartListInner";
    }


}
