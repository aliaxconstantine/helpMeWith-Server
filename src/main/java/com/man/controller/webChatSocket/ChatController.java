package com.man.controller.webChatSocket;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.man.dto.ChatMessageForm;
import com.man.dto.RabbitMessage;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import static com.man.dto.RabbitMessage.CHAT_MESSAGE_UPDATE_ROUTING_KEY;

@Controller
@Log4j2
@ServerEndpoint("/websocket/{userId}")
public class ChatController{
    //聊天室
    public static final ConcurrentHashMap<String, Session> webSocketSet = new ConcurrentHashMap<>();
    public Session session;
    public static RabbitTemplate rabbitTemplate;

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate){
        ChatController.rabbitTemplate = rabbitTemplate;
    }

    public ChatController(){}


    //用户上线了
    @OnOpen
    public void onOpen(Session webSocketsession,@PathParam(value = "userId") String pubId){
        this.session = webSocketsession;
        webSocketSet.put(pubId, webSocketsession);
        log.info("[websocket消息]用户"+ pubId+"上线。");
    }

    //用户下线了
    @OnClose
    public void onClose(Session session, CloseReason closeReason,@PathParam(value = "userId") String pubId) throws IOException {
           session.close();
           webSocketSet.remove(String.valueOf(pubId));
           log.info("[websocket消息]用户"+ pubId +"下线。");

    }

    //用户发送消息
    @OnMessage(maxMessageSize = 56666)
    public void onMessage(Session session,String message,@PathParam(value = "userId") String pubId) throws IOException {
        log.info("[websocket消息]用户"+ pubId +"发送消息"+message);
        //发送消息延迟保存
        webSocketSet.forEach((key, value) -> {
            //如果是心跳消息
            if(message.equals("ping")){
                session.getUserProperties().put("lastHeartbeatTime", System.currentTimeMillis());

            }
            else{
                ChatMessageForm chatMessageForm = JSONUtil.toBean(message, ChatMessageForm.class);
                if (key.equals(StrUtil.toString(chatMessageForm.getPublishId()))) {
                    try {
                        value.getBasicRemote().sendText(message);
                        rabbitTemplate.convertAndSend(RabbitMessage.EXCHANGE_NAME, CHAT_MESSAGE_UPDATE_ROUTING_KEY, message);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }

        });

    }

    @OnError
    public void onError(Session session,Throwable throwable,@PathParam(value = "userId") String pubId) throws IOException {
        log.error("[websocket]发生错误:"+throwable.getMessage());
        session.close();
    }

}
