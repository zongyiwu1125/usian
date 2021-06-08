package com.usian.controller;

import com.usian.pojo.TbUser;
import com.usian.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/getUserByToken/{token}")
    public TbUser getUserByToken(@PathVariable String token){
        return userService.getUserByToken(token);
    }

//    @RequestMapping("/getUserByToken/{token}")
//    public boolean getUserByToken(@PathVariable String token){
//        return userService.getUserByToken(token);
//    }

    //判断用户名或手机号是否已被注册
    @RequestMapping("/checkUserInfo/{checkValue}/{checkFlag}")
    public boolean checkUserInfo(@PathVariable String checkValue, @PathVariable Integer checkFlag){
        return userService.checkUserInfo(checkValue,checkFlag);
    }

    //用户信息注册
    @RequestMapping("/userRegister")
    public void userRegister(@RequestBody TbUser user){
        userService.userRegister(user);
    }

    //登录
    @RequestMapping("/userLogin")
    public Map<String,String> userLogin(@RequestParam("username")String username, @RequestParam("password")String password){
        return userService.userLogin(username,password);
    }

    //退出登录
    @RequestMapping("/logOut")
    public boolean logOut(@RequestParam("token") String token){
        return userService.logOut(token);
    }
}
