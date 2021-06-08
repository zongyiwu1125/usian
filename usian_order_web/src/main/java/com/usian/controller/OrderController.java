package com.usian.controller;

import com.usian.api.OrderFeign;
import com.usian.pojo.TbOrder;
import com.usian.pojo.TbOrderShipping;
import com.usian.utils.Result;
import com.usian.vo.CartItemVo;
import com.usian.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/frontend/order")
public class OrderController {

    @Autowired
    private OrderFeign orderFeign;

    @RequestMapping("/goSettlement")
    public Result goSettlement(@RequestParam("ids") String[] ids,@RequestParam("userId") String userId,@RequestParam("token") String token){
        List<CartItemVo> cartItemVoList=orderFeign.goSettlement(ids,userId);
        return Result.ok(cartItemVoList);
    }

    @RequestMapping("/insertOrder")
    public Result insertOrder(String orderItem, TbOrder order, TbOrderShipping orderShipping){
        String orderId=orderFeign.insertOrder(new OrderVo(orderItem,order,orderShipping));
        return Result.ok(orderId);
    }
}
