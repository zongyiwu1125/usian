package com.usian.scheduled;

import com.usian.mapper.SendMessageLogMapper;
import com.usian.mq.OrderSendMessage;
import com.usian.pojo.SendMessageLog;
import com.usian.service.OrderService;
import com.usian.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class OrderScheduled {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderSendMessage orderSendMessage;

    @Autowired
    private SendMessageLogMapper sendMessageLogMapper;

    @Scheduled(cron="0 0/1 * * * ?")
    public void scheduledMethod(){
        //修改订单状态  超过30分钟未支付修改订单状态
        orderService.updateStatus();
    }


    @Scheduled(cron="0/10 * * * * ?")
    public void sendOrderMessage(){
        //读取待发送的消息
        SendMessageLog sendMessageLog = new SendMessageLog();
        sendMessageLog.setStatus(0);
        List<SendMessageLog> list = sendMessageLogMapper.select(sendMessageLog);
        //重新往mq发信息
        list.forEach(message->{
            Map map = JsonUtils.jsonToMap(message.getBody(), String.class, Integer.class);
            //发送信息
            orderSendMessage.sendMessage(message.getId()+"",map);
        });
    }
}
