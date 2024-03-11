package com.man.controller;

import com.man.dto.HttpResult;
import com.man.entity.core.UserLikeMessage;
import com.man.service.CoreService.TaskMessageService;
import com.man.service.CoreService.UserLikeMessageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//任务消息控制
@ResponseBody
@RestController
@Log4j2
@RequestMapping(value = "/taskMessage",produces = {"application/json;charset=utf-8"})
public class TaskMessageController {
    private final UserLikeMessageService userLikeMessageService;
    private final TaskMessageService taskMessageService;
    @Autowired
    private TaskMessageController(UserLikeMessageService userLikeMessageService, TaskMessageService taskMessageService){
        this.userLikeMessageService = userLikeMessageService;
        this.taskMessageService = taskMessageService;
    }

    //给任务下的对话点赞
    @ResponseBody
    @PostMapping("/star")
    public HttpResult addLikeOnTaskMessage(@RequestParam String messageId){
        return userLikeMessageService.addLike(messageId);
    }

    //给任务下的对话点赞
    @ResponseBody
    @DeleteMapping("/star")
    public HttpResult delLikeOnTaskMessage(@RequestParam String messageId){
        return userLikeMessageService.delLike(messageId);
    }

    //获取任务下点赞数
    //给任务下的对话点赞
    @ResponseBody
    @GetMapping("/star")
    public HttpResult getLikeOnTaskMessage(@RequestParam String messageId){
        return taskMessageService.getLike(messageId);
    }

    //获取该用户有没有给该任务点赞
    @ResponseBody
    @GetMapping("/star/status")
    public HttpResult getOkStar(@RequestParam String messageId){
        return userLikeMessageService.getOk(messageId);
    }
}
