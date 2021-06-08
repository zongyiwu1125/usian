package com.usian.interceptor;


import com.usian.api.UserFeign;
import com.usian.pojo.TbUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserFeign userFeign;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //判断用户是否登录
        String token = request.getParameter("token");
        if (!StringUtils.isNotEmpty(token)){
            return false;
        }
        TbUser user = userFeign.getUserByToken(token);
        if (user==null){
            return false;
        }
        return true;
    }

}
