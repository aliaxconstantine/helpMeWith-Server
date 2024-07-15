package com.HelpMe.ServiceListener;

import cn.hutool.json.JSONUtil;
import com.HelpMe.dto.ChatMessageForm;
import com.HelpMe.dto.RabbitMessage;
import com.HelpMe.Service.CoreService.CommunicationsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Log4j2
@Component
public class ChatListener {
    public final CommunicationsService communicationsService;

    @Autowired
    public ChatListener(CommunicationsService communicationsService) {
        this.communicationsService = communicationsService;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = RabbitMessage.CHAT_MESSAGE_UPDATE_QUEUE),
            exchange = @Exchange(name = RabbitMessage.EXCHANGE_NAME),
            key = {RabbitMessage.CHAT_MESSAGE_UPDATE_ROUTING_KEY})
    )
    public void saveMessageQueue(String message){
        ChatMessageForm chatMessageForm = JSONUtil.toBean(message, ChatMessageForm.class);
        communicationsService.sendChatMessage(chatMessageForm.getMessage(),chatMessageForm.getTargetId().toString(),chatMessageForm.getPublishId());
    }
}
