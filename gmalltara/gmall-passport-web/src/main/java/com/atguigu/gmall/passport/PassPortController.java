package com.atguigu.gmall.passport;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.CartInfo;
import com.atguigu.gmall.bean.UserInfo;
import com.atguigu.gmall.util.CookieUtil;
import com.atguigu.gmall.util.JwtUtil;
import com.atguigu.gmall.service.CartService;
import com.atguigu.gmall.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PassPortController {
    @Reference
    UserService userService;

    @Reference
    CartService cartService;

    @RequestMapping("index")
    public String index(String returnUrl,Map map){
        map.put("returnUrl",returnUrl);
        System.out.println("登录界面获得的url"+returnUrl);
        return "index";
    }

    //登录  颁发token
    @RequestMapping("login")
    public String login(HttpServletRequest request, HttpServletResponse response, UserInfo userInfo){
        UserInfo userInfo1=userService.login(userInfo);
        if(userInfo1==null){
            return "username or password err";
        }
        Map<String,String>map=new HashMap<>();
        map.put("userId",userInfo1.getId());
        map.put("nickName",userInfo1.getNickName());
        String token = JwtUtil.encode("atguigu", map, getMyIp(request));

        //登录陈功  合并购物车
        String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
        List<CartInfo> cartInfos=null;  //cookie购物车
        if(StringUtils.isNotBlank(cartListCookie)){
            cartInfos = JSON.parseArray(cartListCookie, CartInfo.class);
        }
        cartService.combineCart(cartInfos,userInfo1.getId());
        CookieUtil.setCookie(request,response,"cartListCookie","",0,true);
        return token;
    }

    private String getMyIp(HttpServletRequest request) {
        String ip="";
        ip=request.getHeader("x-forward-for");
        if(StringUtils.isBlank(ip)){
            ip=request.getRemoteAddr();
        }
        if(StringUtils.isBlank(ip)){
            ip="127.0.0.1";
        }
        return ip;
    }

    //校验token
    @ResponseBody
    @RequestMapping("verify")
    public String verify(String token,String salt){
        Map<String,String>userMap=null;
        try {
            userMap= JwtUtil.decode("atguigu",token,salt);
        }catch (Exception e){
            return "fail";
        }
        if(userMap!=null){
            return "success";
        }
        else{
            return "fail";
        }
    }
    }
