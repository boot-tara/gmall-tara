package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.CartInfo;

import java.util.List;

public interface CartService {
    CartInfo ifCartExists(CartInfo cartInfo);

    void updateCart(CartInfo cartInfoDb);

    void save(CartInfo cartInfoDb);

    void syncCache(String userId);

    List<CartInfo> getCartCache(String userId);

    void updateCartChecked(CartInfo cartInfo);

    List<CartInfo> getCartCacheByChecked(String userId);

    void deleteCartById(List<CartInfo> cartInfos);

    void combineCart(List<CartInfo> cartInfos, String id);
}
