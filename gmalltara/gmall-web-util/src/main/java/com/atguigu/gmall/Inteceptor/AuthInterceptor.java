package com.atguigu.gmall.Inteceptor;

import com.atguigu.gmall.annotation.LoginRequire;
import com.atguigu.gmall.util.CookieUtil;
import com.atguigu.gmall.util.JwtUtil;
import com.atguigu.util.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获得带注解的方法
        HandlerMethod method = (HandlerMethod) handler;
        LoginRequire methodAnnotation = method.getMethodAnnotation(LoginRequire.class);
        if(methodAnnotation==null){
            return true;              //没有注解，方形
        }

        //获取token
        String token="";
        String oldToken= CookieUtil.getCookieValue(request,"oldToken",true);
        String newToken=request.getParameter("newToken");


        if(StringUtils.isNotBlank(oldToken)&& StringUtils.isBlank(newToken)){
            //登陆过
            token=oldToken;
        }
        if(StringUtils.isBlank(oldToken)&&StringUtils.isNotBlank(newToken)){
            //第一次登陆
            token = newToken;
        }

        if(StringUtils.isNotBlank(oldToken)&&StringUtils.isNotBlank(newToken)){
            //登陆过期
            token = newToken;
        }

        //校验token
        if(methodAnnotation.isNeedSuccess()&&StringUtils.isBlank(token)){
            StringBuffer requestURL = request.getRequestURL();
            System.out.println("requestURL=========="+requestURL);
            response.sendRedirect("http://localhost:8085/index?returnUrl="+requestURL);
            return false;
        }


        String success="";
        if(StringUtils.isNotBlank(token)){
            success= HttpClientUtil.doGet("http://localhost:8085/verify?token="+token+"&salt="+getMyIp(request));
        }
        //分析校验结果
        if(!"success".equals(success)&&methodAnnotation.isNeedSuccess()){
            response.sendRedirect("http://localhost:8085/index");
            return false;
        }
        if(!success.equals("success")&&!methodAnnotation.isNeedSuccess())
        {
            // 购物车方法

            return true;
        }

        //用户信息存放request作用域

        if(success.equals("success")){
            CookieUtil.setCookie(request,response,"oldToken",token,60*60*2,true);//刷新老token
            Map userMap = JwtUtil.decode("atguigu0328", token, getMyIp(request));
            request.setAttribute("userId",userMap.get("userId"));
            request.setAttribute("nickName",userMap.get("nickName"));
        }

        return true;
    }

    private String getMyIp(HttpServletRequest request) {
        String ip="";
        ip=request.getHeader("x-forward-for");
        if(StringUtils.isBlank(ip)){
            ip = request.getRemoteAddr();
        }
        if(StringUtils.isBlank(ip)){
            ip="127.0.0.1";
        }
        return ip;
    }
}
