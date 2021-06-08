package com.usian.controller;

import com.usian.service.CartService;
import com.usian.vo.CartItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("cart")
public class CartController {
    @Autowired
    private CartService cartService;
    //添加购物车项
    @RequestMapping("/addItem")
    void addItem(@RequestParam("userId")Long userId, @RequestParam("itemId") Long itemId){
        cartService.addItem(userId,itemId);
    }

    //查询购物车列表
    @RequestMapping("/showCart")
    public List<CartItemVo> showCart(@RequestParam("userId")Long userId){
        return cartService.showCart(userId);
    }

    //购物车数量变化
    @RequestMapping("/updateItemNum")
    public void updateItemNum(@RequestParam("num") Integer num,@RequestParam("userId")Long userId,@RequestParam("itemId")Long itemId){
        cartService.updateItemNum(num,userId,itemId);
    }

    //删除购物项
    @RequestMapping("/deleteItemFromCart")
    void deleteItemFromCart(@RequestParam("itemId")Long itemId,@RequestParam("userId") Long userId){
        cartService.deleteItemFromCart(itemId,userId);
    }
}
