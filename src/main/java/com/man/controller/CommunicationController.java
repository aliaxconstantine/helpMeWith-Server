package com.man.controller;
import com.man.dto.HttpResult;
import com.man.service.CoreService.CommunicationsService;
import com.man.service.CoreService.FriendService;
import com.man.service.CoreService.TRechatService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

//用户聊天接口
@Controller
@ResponseBody
@Log4j2
@RequestMapping(value = "/chat",produces = {"application/json;charset=utf-8"})
public class CommunicationController {
    private final CommunicationsService chatService;
    private final FriendService friendService;

    private final TRechatService tRechatService;
    @Autowired
    public CommunicationController(CommunicationsService chatService, FriendService friendService, TRechatService tRechatService) {
        this.chatService = chatService;
        this.friendService = friendService;
        this.tRechatService = tRechatService;
    }


    //获取与某用户消息
    @GetMapping("/{userId}")
    public HttpResult getChatMessagesByUserId(@PathVariable String userId) {
        return chatService.getChatMessagesByUserId(userId);
    }

    //删除消息
    @DeleteMapping("/{chatId}")
    public HttpResult deleteChatMessageById(@PathVariable String chatId) {
        return chatService.deleteChatMessageById(chatId);
    }

    @GetMapping("/recode/all")
    public HttpResult getAllRecode(){
        return tRechatService.getAll();
    }

}

