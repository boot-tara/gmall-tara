package com.atguigu.gmall.user.service;

import com.atguigu.gmall.bean.UserAddress;
import com.atguigu.gmall.bean.UserInfo;
import com.atguigu.gmall.user.mapper.UserAddressMapper;
import com.atguigu.gmall.user.mapper.UserInfoMapper;
import com.atguigu.gmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class UserServiceImpl implements UserService {

    @Autowired
    UserAddressMapper userAddressMapper;

    @Autowired
    RedisTemplate<String,UserInfo>redisTemplate;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public List<UserAddress> getUserAddressList(String userId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        List<UserAddress> select = userAddressMapper.select(userAddress);
        return select;
    }

    @Override
    public UserInfo login(UserInfo userInfo) {
        //从缓存中获取用户
        String key="user:"+userInfo.getLoginName()+":info";
         userInfo = redisTemplate.opsForValue().get(key);
        if(userInfo==null){
            //数据库查
            userInfo=userInfoMapper.selectUser(userInfo);
            if(userInfo!=null){
                redisTemplate.opsForValue().set(key,userInfo,60*60*2, TimeUnit.SECONDS);
            }
        }
        return userInfo;
    }
}
