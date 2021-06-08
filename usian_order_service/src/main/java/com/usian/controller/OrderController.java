package com.usian.controller;

import com.usian.service.OrderService;
import com.usian.vo.CartItemVo;
import com.usian.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("/insertOrder")
    public String insertOrder(@RequestBody OrderVo orderVo){
        return orderService.insertOrder(orderVo);
    }

    @RequestMapping("/goSettlement")
    public List<CartItemVo> goSettlement(@RequestParam("ids") String[] ids, @RequestParam("userId") String userId){
        return orderService.goSettlement(ids,userId);
    }
}
