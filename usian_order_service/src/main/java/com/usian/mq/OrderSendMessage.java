package com.usian.mq;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OrderSendMessage {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private OrderConfirmOrReturn orderConfirmOrReturn;

    public void sendMessage(String sendMessageLogId, Map<String, Integer> map){
        //设置回调函数
        rabbitTemplate.setConfirmCallback(orderConfirmOrReturn);
        rabbitTemplate.setReturnCallback(orderConfirmOrReturn);

        //发送信息
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(sendMessageLogId);
        rabbitTemplate.convertAndSend("item_num","update.itemNum",map,correlationData);

    }
}
