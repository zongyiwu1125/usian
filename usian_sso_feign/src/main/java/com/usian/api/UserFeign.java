package com.usian.api;

import com.usian.pojo.TbUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient("usian-sso-service")
public interface UserFeign {

    //退出登录
    @RequestMapping("user/logOut")
    public boolean logOut(@RequestParam("token") String token);

    //获取登录的用户名
    @RequestMapping("user/getUserByToken/{token}")
    public TbUser getUserByToken(@PathVariable String token);

//    获取登录的用户名
//    @RequestMapping("user/getUserByToken/{token}")
//    public boolean getUserByToken(@PathVariable String token);

    //判断用户名或手机号是否已被注册
    @RequestMapping("user/checkUserInfo/{checkValue}/{checkFlag}")
    public boolean checkUserInfo(@PathVariable String checkValue, @PathVariable Integer checkFlag);

    //用户信息注册
    @RequestMapping("user/userRegister")
    public void userRegister(@RequestBody TbUser user);

    //登录
    @RequestMapping("user/userLogin")
    public Map<String,String> userLogin(@RequestParam("username")String username, @RequestParam("password")String password);
}
