package com.atguigu.gmall.order.service;

import com.atguigu.gmall.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class OrderServiceImpl implements OrderService {

    @Autowired
    RedisTemplate<String,String>redisTemplate;

    @Override
    public String genTradeCode(String userId) {
        String key="user:"+userId+":tradeCode";
        String value= UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(key,value,60*60*2, TimeUnit.SECONDS);//交易码存在2小时
        return value;
    }

    //验证交易码
    @Override
    public boolean checkTradeCode(String tradeCode, String userId) {
        String key="user:"+userId+":tradeCode";
        String s = redisTemplate.opsForValue().get(key);
        if(StringUtils.isNotBlank(s)){
            if(s.equals(tradeCode)){
                //删除交易码
             redisTemplate.delete(key);
             return true;
            }
        }
        return false;
    }
}
