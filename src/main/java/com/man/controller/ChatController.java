package com.man.controller;
import cn.hutool.core.util.StrUtil;
import com.man.dto.HttpResult;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
@Log4j2
@ServerEndpoint("/voice/{initId}/{publishId}")
public class ChatController{
    //聊天室
    public static final ConcurrentHashMap<String, Session> webSocketSet = new ConcurrentHashMap<String, Session>();
    public Session session;

    //用户上线了
    @OnOpen
    public void onOpen(@PathParam("initId") Long initId ,@PathParam("publishId") Long pubId,Session webSocketsession){
        this.session = webSocketsession;
        webSocketSet.put(String.valueOf(initId), webSocketsession);
    }

    //用户下线了
    @OnClose
    public void onClose(@PathParam("initId") Long initId ,@PathParam("publishId") Long pubId,Session session, CloseReason closeReason) throws IOException {
        session.close();
        webSocketSet.remove(String.valueOf(initId));
    }

    //用户发送消息
    @OnMessage(maxMessageSize = 56666)
    public void onMessage(@PathParam("initId") Long initId,@PathParam("publishId") Long pubId,Session session,String message) throws IOException {
        webSocketSet.forEach((key, value) -> {
            if (key.equals(StrUtil.toString(pubId))) {
                try {
                    value.getBasicRemote().sendText(message);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    @OnError
    public void onError(Session session,Throwable throwable) throws IOException {
        log.error("发生错误:"+throwable.getMessage());
        session.close();
    }

    @GetMapping("/flag/{id}")
    public HttpResult getFlag(@PathVariable Long id){
        AtomicBoolean flag = new AtomicBoolean(false);
        webSocketSet.forEach((key, value) -> {
            if (key.equals(StrUtil.toString(id))) {
                flag.set(true);
            }
        });
        return HttpResult.success(flag.get());
    }

}
