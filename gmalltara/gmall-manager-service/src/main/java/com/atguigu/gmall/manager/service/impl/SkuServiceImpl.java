package com.atguigu.gmall.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.SkuInfo;
import com.atguigu.gmall.manager.mapper.SkuInfoMapper;
import com.atguigu.gmall.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    SkuInfoMapper skuInfoMapper;

    @Autowired
    RedisTemplate<String,SkuInfo> redisTemplate;

    @Override
    public SkuInfo getSkuInfoBySkuId(String skuId) {
        String key="sku:"+skuId+":info";
        SkuInfo skuInfo = redisTemplate.opsForValue().get(key);
        if(skuInfo==null){
            //查询数据库
            skuInfo=skuInfoMapper.selectFromDb(skuId);
            if(skuInfo!=null){//在缓存
                redisTemplate.opsForValue().set(key,skuInfo,60*60*2, TimeUnit.SECONDS);//存放2小时
            }
        }

        return skuInfo;
    }


}
