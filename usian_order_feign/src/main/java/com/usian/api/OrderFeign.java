package com.usian.api;

import com.usian.vo.CartItemVo;
import com.usian.vo.OrderVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("usian-order-service")
public interface OrderFeign {

    @RequestMapping("order/insertOrder")
    public String insertOrder(@RequestBody OrderVo orderVo);

    @RequestMapping("order/goSettlement")
    public List<CartItemVo> goSettlement(@RequestParam("ids") String[] ids, @RequestParam("userId") String userId);
}
