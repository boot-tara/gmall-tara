package com.atguigu.gmall.user.mapper;

import com.atguigu.gmall.bean.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper {
    UserInfo selectUser(UserInfo userInfo);
}
