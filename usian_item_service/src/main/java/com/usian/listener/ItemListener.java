package com.usian.listener;

import com.rabbitmq.client.Channel;
import com.usian.mapper.ReceiveMessageLogMapper;
import com.usian.pojo.ReceiveMessageLog;
import com.usian.service.ItemService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class ItemListener {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ReceiveMessageLogMapper receiveMessageLogMapper;

    //修改商品库存
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value="item_num_queue",durable = "true"),
            exchange = @Exchange(value="item_num",type= ExchangeTypes.TOPIC),
            key= {"update.itemNum"}
    ))
    public void updateItemNumByItemId(Map<String, Integer> map, Channel channel, Message message){

        //判断信息是否被处理
        String messageId = (String) message.getMessageProperties().getHeaders().get("spring_returned_message_correlation");
        ReceiveMessageLog receiveMessageLog = receiveMessageLogMapper.selectByPrimaryKey(messageId);

        if (receiveMessageLog==null){//没有处理过
            itemService.updateItemNumByItemId(map);
            //增加记录
            receiveMessageLog = new ReceiveMessageLog();
            receiveMessageLog.setId(messageId);
            receiveMessageLogMapper.insertSelective(receiveMessageLog);
        }

        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
