package com.atguigu.gmall.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.CartInfo;
import com.atguigu.gmall.bean.OrderDetail;
import com.atguigu.gmall.bean.UserAddress;
import com.atguigu.gmall.annotation.LoginRequire;
import com.atguigu.gmall.service.CartService;
import com.atguigu.gmall.service.OrderService;
import com.atguigu.gmall.service.SkuService;
import com.atguigu.gmall.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//登录之后才能进入的功能，数据直接缓存读取  页面存放交易码（只可以使用一次，防止重复提交订单）
@Controller
public class OrderController {

    @Reference
    OrderService orderService;

    @Reference
    CartService cartService;

    @Reference
    SkuService skuService;

    @Reference
    UserService userService;

    @LoginRequire(isNeedSuccess = true)
    @RequestMapping("toTrade")
    public String toTrade(HttpServletRequest request, HttpServletResponse response, Map map){
        String userId=(String)request.getAttribute("userId");
        List<CartInfo>cartInfos=cartService.getCartCacheByChecked(userId);  //改了小框框之后，缓存中有了isChecked为0的购物车
        //将购物车集合封装成orderDetail集合
        List<OrderDetail>orderDetails=new ArrayList<>();
        for (CartInfo cartInfo : cartInfos) {
            OrderDetail orderDetail = new OrderDetail();
            // 将购物车对象转化为订单对象
            orderDetail.setOrderPrice(cartInfo.getCartPrice());
            orderDetail.setSkuId(cartInfo.getSkuId());
            orderDetail.setSkuName(cartInfo.getSkuName());
            orderDetails.add(orderDetail);
        }
        //选出收货地址
        List<UserAddress> userAddressList = userService.getUserAddressList(userId);

        //页面生成唯一交易码
        String tradeCode = orderService.genTradeCode(userId);
        map.put("tradeCode", tradeCode);
        map.put("userAddressList", userAddressList);
        map.put("orderDetailList", orderDetails);
        map.put("totalAmount", getTotalPrice(cartInfos));
        return "trade";
    }

    @LoginRequire(isNeedSuccess = true)
    @RequestMapping("submitOrder")
    public String submitOrder(String tradeCode, HttpServletRequest request, HttpServletResponse response,Map map){
        String userId=(String)request.getAttribute("userId");
        boolean btardeCode=orderService.checkTradeCode(tradeCode,userId);  //验证交易码
        if(btardeCode){
            //可以交易  删除数据库缓存
            List<CartInfo> cartInfos = cartService.getCartCacheByChecked(userId);//交易的购物车集合
            System.out.println("删除前从缓存中取得数据+"+cartInfos);
            cartService.deleteCartById(cartInfos);  //清空交易陈功的购物车
            return "success";
        }
        else{
            map.put("errMsg","获取订单信息失败");
            return "tradeFail";
        }


    }

    private BigDecimal getTotalPrice(List<CartInfo> cartInfos) {
        BigDecimal b=new BigDecimal("0");
        for(CartInfo cartInfo:cartInfos){
            if(cartInfo.getIsChecked().equals("1")){
                b=b.add(cartInfo.getCartPrice());
            }
        }
        return b;
    }
}
