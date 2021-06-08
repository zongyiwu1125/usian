package com.usian.service;

import com.usian.mapper.TbUserMapper;
import com.usian.pojo.TbUser;
import com.usian.util.RedisClient;
import com.usian.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private TbUserMapper userMapper;
    @Autowired
    private RedisClient redisClient;


    public boolean checkUserInfo(String checkValue, Integer checkFlag) {
        TbUser user = new TbUser();//查询条件对象
        if (checkFlag==1){
            user.setUsername(checkValue);
        }else {
            user.setPhone(checkValue);
        }
        List<TbUser> users = userMapper.select(user);
        if (users==null||users.size()==0){
            return true;
        }
        return false;
    }

    public void userRegister(TbUser user) {
        //用户密码加密
        user.setPassword(MD5Utils.digest(user.getPassword()));
        //设置默认时间
        user.setCreated(new Date());
        userMapper.insertSelective(user);
    }

    public Map<String,String> userLogin(String username, String password) {
        Map map = new HashMap<>();//用来返回前台数据的集合
        TbUser user = new TbUser();//查询条件对象
        user.setUsername(username);
        user.setPassword(MD5Utils.digest(password));

        List<TbUser> users = userMapper.select(user);
        //生成令牌
        String token = UUID.randomUUID().toString();
        if (users!=null&&users.size()>0){
            user = users.get(0);

//            //存入redis
            redisClient.set(token,user);
            redisClient.expire(token,60*60*24);
            map.put("token",token);
            map.put("userid",user.getId()+"");
            map.put("username",user.getUsername());
        }

        return map;
    }


    public TbUser getUserByToken(String token) {
        TbUser user = (TbUser) redisClient.get(token);
        redisClient.expire(token,60*60*24);
        return user;
    }

    public boolean logOut(String token) {
        try {
            redisClient.del(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


//    public boolean getUserByToken(String token){
//        String publicKey = (String) redisClient.get("PUBLIC_KEY");
//        try {
//            TbUser tbUser = JwtUtils.getInfoFromToken(token, StringUtils.toByteArray(publicKey));
//            if (tbUser!=null){
//                return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
}
