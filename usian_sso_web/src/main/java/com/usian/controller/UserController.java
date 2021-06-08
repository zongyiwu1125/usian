package com.usian.controller;

import com.usian.api.UserFeign;
import com.usian.pojo.TbUser;
import com.usian.utils.CookieUtils;
import com.usian.utils.Result;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/frontend/sso")
public class UserController {

    @Autowired
    private UserFeign userFeign;
    @Autowired
    private AmqpTemplate amqpTemplate;

    @RequestMapping("/checkUserInfo/{checkValue}/{checkFlag}")
    public Result checkUserInfo(@PathVariable String checkValue,@PathVariable Integer checkFlag){
        boolean result=userFeign.checkUserInfo(checkValue,checkFlag);
        if (result){
            return Result.ok();
        }else {
            return Result.error("已存在！！！");
        }
    }

    @RequestMapping("/userRegister")
    public Result userRegister(TbUser user){
        try {
            userFeign.userRegister(user);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.error("注册失败");
    }

    @RequestMapping("/userLogin")
    public Result userLogin(@RequestParam("username")String username, @RequestParam("password")String password,
                            HttpServletRequest request, HttpServletResponse response){
        Map<String,String> map=userFeign.userLogin(username,password);
        if (map!=null&&map.size()>0){
            String userid = map.get("userid");
            String cartJson = CookieUtils.getCookieValue(request, "CART", true);
            if (cartJson!=null&&!cartJson.equals("")){
                amqpTemplate.convertAndSend("user_exchange","user.login",cartJson+"---"+userid);
                //登录完成删除cookie数据
                CookieUtils.deleteCookie(request,response,"CART");
            }
            return Result.ok(map);
        }
        return Result.error("登录失败");
    }

    //获取登录的用户名
    @RequestMapping("/getUserByToken/{token}")
    public Result getUserByToken(@PathVariable String token){
        TbUser user=userFeign.getUserByToken(token);
        if (user!=null){
            return Result.ok();
        }
        return Result.error("登录失效");
    }

    //获取登录的用户名
//    @RequestMapping("/getUserByToken/{token}")
//    public Result getUserByToken(@PathVariable String token){
//        boolean b=userFeign.getUserByToken(token);
//        if (b==true){
//            return Result.ok();
//        }
//        return Result.error("登录失效");
//    }

    //退出登录
    @RequestMapping("/logOut")
    public Result logOut(@RequestParam("token") String token){
         boolean b = userFeign.logOut(token);
         if (b){
             return Result.ok();
         }
         return Result.error("退出失败");
    }

}
