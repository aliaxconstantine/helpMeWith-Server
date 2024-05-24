package com.man.controller.config.rabbitMqConfig;

import com.man.dto.RabbitMessage;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 艾莉希雅
 */
@Configuration
public class RabbitMqConfig {
    public static final String EXCHANGE_NAME = RabbitMessage.EXCHANGE_NAME;
    public static final String TASK_QUEUE_NAME = RabbitMessage.ORDER_UPDATE_QUEUE;
    public static final String TASK_ROUTING_KEY  = RabbitMessage.ORDER_UPDATE_ROUTING_KEY;
    public static final String QUEUE_NAME = RabbitMessage.CHAT_MESSAGE_UPDATE_QUEUE;
    public static final String ROUTING_KEY = RabbitMessage.CHAT_MESSAGE_UPDATE_ROUTING_KEY;

    @Bean
    public Exchange taskExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue taskQueue() {
        return new Queue(TASK_QUEUE_NAME);
    }



    @Bean
    public Binding taskBinding() {
        return BindingBuilder.bind(taskQueue()).to(taskExchange()).with(TASK_ROUTING_KEY).noargs();
    }
    @Bean
    public Queue chatMessageQueue() {
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public Binding chatMessageBinding() {
        return BindingBuilder.bind(chatMessageQueue()).to(taskExchange()).with(ROUTING_KEY).noargs();
    }


}
