package com.usian.service;

import com.usian.mapper.SendMessageLogMapper;
import com.usian.mapper.TbOrderItemMapper;
import com.usian.mapper.TbOrderMapper;
import com.usian.mapper.TbOrderShippingMapper;
import com.usian.mq.OrderSendMessage;
import com.usian.pojo.SendMessageLog;
import com.usian.pojo.TbOrder;
import com.usian.pojo.TbOrderItem;
import com.usian.pojo.TbOrderShipping;
import com.usian.util.RedisClient;
import com.usian.utils.JsonUtils;
import com.usian.vo.CartItemVo;
import com.usian.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private TbOrderMapper orderMapper;

    @Autowired
    private TbOrderItemMapper orderItemMapper;

    @Autowired
    private TbOrderShippingMapper orderShippingMapper;

    @Autowired
    private SendMessageLogMapper sendMessageLogMapper;

    @Autowired
    private OrderSendMessage orderSendMessage;



    public List<CartItemVo> goSettlement(String[] ids, String userId) {
        Map<Object, Object> cartMap = redisClient.hgetAll("CART" + userId);
        ArrayList<CartItemVo> cartItemVoList = new ArrayList<>();
        Set<Object> itemIds = cartMap.keySet();
        for (String id : ids) {
            for (Object itemId : itemIds) {
                if (id.equals(itemId)){
                    cartItemVoList.add((CartItemVo) cartMap.get(itemId));
                    break;
                }
            }
        }

        return cartItemVoList;
    }

    @Transactional
    public String insertOrder(OrderVo orderVo) {
        //生成订单id
        String orderId = UUID.randomUUID().toString();
        //订单生成时间
        Date now = new Date();

        //订单
        TbOrder order = orderVo.getOrder();
        order.setOrderId(orderId);
        order.setStatus(1);
        order.setCreateTime(now);
        //添加订单
        orderMapper.insertSelective(order);

        //获取订单项json格式数据
        String orderItemJson = orderVo.getOrderItem();
        List<TbOrderItem> orderItemList = JsonUtils.jsonToList2(orderItemJson, TbOrderItem.class);
        for (TbOrderItem orderItem : orderItemList) {
            //生成订单项id
            String id = UUID.randomUUID().toString();
            orderItem.setId(id);
            orderItem.setOrderId(orderId);
        }
        orderItemMapper.addOrderItem(orderItemList);

        //地址表
        TbOrderShipping orderShipping = orderVo.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(now);
        orderShippingMapper.insertSelective(orderShipping);

        //修改库存
        Map<String, Integer> map = orderItemList.stream().collect(Collectors.toMap(TbOrderItem::getItemId, TbOrderItem::getNum));

        //先往发送消息记录表中添加数据
        SendMessageLog sendMessageLog = new SendMessageLog();
        sendMessageLog.setBody(JsonUtils.objectToJson(map));
        sendMessageLogMapper.insertSelective(sendMessageLog);

        //往mq发信息
        orderSendMessage.sendMessage(sendMessageLog.getId()+"",map);

        return orderId;
    }

    public void updateStatus() {
        orderMapper.updateStatus();
    }
}
