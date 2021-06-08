package com.usian.api;

import com.usian.vo.CartItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "usian-cart-service")
public interface CartFeign {
    //添加购物车项
    @RequestMapping("cart/addItem")
    public void addItem(@RequestParam("userId")Long userId,@RequestParam("itemId") Long itemId);

    //查询购物车列表
    @RequestMapping("cart/showCart")
    public List<CartItemVo> showCart(@RequestParam("userId")Long userId);

    //购物车数量变化
    @RequestMapping("cart/updateItemNum")
    public void updateItemNum(@RequestParam("num") Integer num,@RequestParam("userId")Long userId,@RequestParam("itemId")Long itemId);

    //删除购物项
    @RequestMapping("cart/deleteItemFromCart")
    void deleteItemFromCart(@RequestParam("itemId")Long itemId,@RequestParam("userId") Long userId);
}
