package com.man.ServiceListener;

import com.man.dto.RabbitMessage;
import com.man.service.CoreService.SysMessageService;
import com.man.service.CoreService.TasksService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskListener {

    private final TasksService tasksService;
    private final SysMessageService sysMessageService;

    @Autowired
    public TaskListener(TasksService tasksService, SysMessageService sysMessageService) {
        this.tasksService = tasksService;
        this.sysMessageService = sysMessageService;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = RabbitMessage.ORDER_UPDATE_QUEUE),
            exchange = @Exchange(name = RabbitMessage.EXCHANGE_NAME),
            key = {RabbitMessage.ORDER_UPDATE_ROUTING_KEY})
    )
    public void listenUpdateQueue(String taskId){
        tasksService.successTask(taskId);
    }
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = RabbitMessage.SYSTEM_INFO_QUEUE),
            exchange = @Exchange(name = RabbitMessage.EXCHANGE_NAME),
            key = {RabbitMessage.SYSTEM_INFO_ROUTING_KEY})
    )
    public void listenMessageQueue(String userId,String message){
        sysMessageService.sendMessage(userId,message);
    }


}
