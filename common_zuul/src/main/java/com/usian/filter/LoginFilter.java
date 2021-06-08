package com.usian.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.usian.api.UserFeign;
import com.usian.pojo.TbUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class LoginFilter extends ZuulFilter {
    @Autowired
    private UserFeign userFeign;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        // 获取请求上下文
        RequestContext rc = RequestContext.getCurrentContext();
        HttpServletRequest request = rc.getRequest();
        //判断用户是否登录
        String token = request.getParameter("token");
        if (!StringUtils.isNotEmpty(token)){
            return false;
        }
        TbUser user = userFeign.getUserByToken(token);
        if (user==null){
            rc.setSendZuulResponse(false);// 代表请求结束。不在继续向下请求
            rc.setResponseStatusCode(401);// 添加一个响应的状态码
            return false;
        }
        return null;
    }
}
