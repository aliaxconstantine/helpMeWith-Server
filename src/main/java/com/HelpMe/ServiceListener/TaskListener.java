package com.HelpMe.ServiceListener;

import cn.hutool.json.JSONUtil;
import com.HelpMe.dto.RabbitMessage;
import com.HelpMe.dto.SystemMessageForm;
import com.HelpMe.Entity.core.Task;
import com.HelpMe.Service.CoreService.SysMessageService;
import com.HelpMe.Service.CoreService.TasksService;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
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
    public void listenUpdateQueue(String task){
        Task upTaskY = JSONUtil.toBean(task, Task.class);
        Task upTask = tasksService.getById(upTaskY.getId());
        if(upTask != null){
            upTask.setStatus(upTaskY.getStatus());
            tasksService.update(upTask);
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = RabbitMessage.SYSTEM_INFO_QUEUE),
            exchange = @Exchange(name = RabbitMessage.EXCHANGE_NAME),
            key = {RabbitMessage.SYSTEM_INFO_ROUTING_KEY})
    )
    public void listenMessageQueue(String message){
        SystemMessageForm systemMessageForm = JSONUtil.toBean(message,SystemMessageForm.class);
        sysMessageService.sendMessage(systemMessageForm.getUserId(),systemMessageForm.getMessage());
    }


}
