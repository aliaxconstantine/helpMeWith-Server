package com.man.controller;

import com.man.dto.ChatMessageFrom;
import com.man.dto.HttpResult;
import com.man.service.CoreService.CommunicationsService;
import com.man.service.CoreService.FriendService;
import com.man.utils.AuthenticationUtils;
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

    @Autowired
    public CommunicationController(CommunicationsService chatService, FriendService friendService) {
        this.chatService = chatService;
        this.friendService = friendService;
    }

    //发送消息
    @PostMapping("/send")
    public HttpResult sendChatMessage(@RequestBody ChatMessageFrom chat) {
        //判断是否为好友
        boolean flag = friendService.isFriend(AuthenticationUtils.getId().toString(),chat.getId().toString());
        if(!flag){
            return HttpResult.fail("你和对方并不是好友，清先添加好友");
        }
        return chatService.sendChatMessage(chat.getMessage(), chat.getId().toString());
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

    //发送图片

}

