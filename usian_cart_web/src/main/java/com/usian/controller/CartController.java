package com.usian.controller;

import com.usian.api.CartFeign;
import com.usian.api.ItemFeign;
import com.usian.pojo.TbItem;
import com.usian.utils.CookieUtils;
import com.usian.utils.JsonUtils;
import com.usian.utils.Result;
import com.usian.vo.CartItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/frontend/cart")
public class CartController {

    @Autowired
    private ItemFeign itemFeign;

    @Autowired
    private CartFeign cartFeign;

    //删除购物项
    @RequestMapping("/deleteItemFromCart")
    public Result deleteItemFromCart(@RequestParam("itemId")Long itemId,@RequestParam("userId")Long userId,HttpServletRequest request,HttpServletResponse response){
        //判断用户是否登录
        if (userId==null){//未登录
            String cartJson = CookieUtils.getCookieValue(request, "CART", true);
            Map map = JsonUtils.jsonToMap(cartJson, Long.class, CartItemVo.class);
            map.remove(itemId);
            CookieUtils.setCookie(request,response,"CART",JsonUtils.objectToJson(map),true);
        }else {//登录
            cartFeign.deleteItemFromCart(itemId,userId);
        }
        return Result.ok();
    }

    //购物车数量变化
    @RequestMapping("/updateItemNum")
    public Result updateItemNum(@RequestParam("num") Integer num,@RequestParam("userId")Long userId,@RequestParam("itemId")Long itemId,HttpServletRequest request,HttpServletResponse response){
        //判断是否登录
        if (userId==null){//未登录
            String cartJson = CookieUtils.getCookieValue(request, "CART", true);
            Map map = JsonUtils.jsonToMap(cartJson, Long.class, CartItemVo.class);
            if (num==0){
                map.remove(itemId);
            }else {
                CartItemVo cartItemVo = (CartItemVo) map.get(itemId);
                cartItemVo.setNum(num);
            }
            CookieUtils.setCookie(request,response,"CART",JsonUtils.objectToJson(map),true);
        }else {//登录
            cartFeign.updateItemNum(num,userId,itemId);
        }
        return Result.ok();
    }

    //查询购物车列表
    @RequestMapping("/showCart")
    public Result showCart(@RequestParam("userId") Long userId,HttpServletRequest request){
        //判断是否登录
        if (userId==null){//没有登录
            String cartJson = CookieUtils.getCookieValue(request, "CART", true);
            Map<Long,CartItemVo> cartMap = JsonUtils.jsonToMap(cartJson,Long.class,CartItemVo.class);
            Collection<CartItemVo> cartCollection = cartMap.values();
            return Result.ok(cartCollection);
        }else {//已登录
            List<CartItemVo> list=cartFeign.showCart(userId);

            return Result.ok(list);
        }
    }

    //添加购物车项
    @RequestMapping("/addItem")
    public Result addItem(@RequestParam("userId") Long userId, @RequestParam("itemId") Long itemId, HttpServletRequest request, HttpServletResponse response){
        //判断用户是否登录
        if (userId==null){//未登录
            //判断是否第一次使用购物车
            String cartJson = CookieUtils.getCookieValue(request, "CART", true);
            if (cartJson==null||cartJson.equals("")){//第一次使用
                HashMap<Long, CartItemVo> cartMap = new HashMap<>();
                addCart(itemId,cartMap);
                CookieUtils.setCookie(request,response,"CART",JsonUtils.objectToJson(cartMap),true);
            }else {//不是第一次使用
                Map cartMap = JsonUtils.jsonToMap(cartJson,Long.class, CartItemVo.class);
                //判断是否是同一个购物车项
                CartItemVo cartItemVo = (CartItemVo) cartMap.get(itemId);
                if (cartItemVo==null){//不是同一个购物车项
                    //新增购物车项
                    addCart(itemId,cartMap);
                }else {//是同一个购物车项
                    cartItemVo.setNum(cartItemVo.getNum()+1);
                }
                CookieUtils.setCookie(request,response,"CART",JsonUtils.objectToJson(cartMap),true);
            }
        }else {//已登录
            cartFeign.addItem(userId,itemId);
        }
        return Result.ok();
    }

    //添加购物车项
    public void addCart(Long itemId,Map cartMap){
        CartItemVo cartItemVo = new CartItemVo();
        TbItem item = itemFeign.selectItemInfo(itemId);
        cartItemVo.setId(itemId);
        cartItemVo.setImage(item.getImage());
        cartItemVo.setNum(1);
        cartItemVo.setPrice(item.getPrice());
        cartItemVo.setSellPoint(item.getSellPoint());
        cartItemVo.setTitle(item.getTitle());
        cartMap.put(itemId,cartItemVo);
    }
}
