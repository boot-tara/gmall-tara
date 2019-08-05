package com.atguigu.gmall.cart.mapper;

import com.atguigu.gmall.bean.CartInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CartInfoMapper {
    CartInfo selectCartInfo(Map<String, String> map);

    void updateCart(CartInfo cartInfoDb);

    void insert(CartInfo cartInfoDb);

    List<CartInfo> selectByUserId(@Param("userId") String userId);

    void update(CartInfo cartInfo);

    void deleteByBatch(@Param("cartInfos") List<CartInfo> cartInfos);
}
