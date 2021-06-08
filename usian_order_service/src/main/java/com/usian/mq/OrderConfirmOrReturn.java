package com.usian.mq;

import com.usian.mapper.SendMessageLogMapper;
import com.usian.pojo.SendMessageLog;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderConfirmOrReturn implements RabbitTemplate.ReturnCallback, RabbitTemplate.ConfirmCallback {

    @Autowired
    private SendMessageLogMapper sendMessageLogMapper;
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack){
            SendMessageLog sendMessageLog = new SendMessageLog();
            sendMessageLog.setId(Long.parseLong(correlationData.getId()));
            sendMessageLog.setStatus(1);
            sendMessageLogMapper.updateByPrimaryKeySelective(sendMessageLog);
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        System.out.println("消息发送失败");
    }
}
