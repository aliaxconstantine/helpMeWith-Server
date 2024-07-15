package com.HelpMe.Controller.WebChatSocket;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Component
@Log4j2
public class HeartbeatScheduler {
    @Scheduled(fixedRate = 50000) // 每隔50秒执行一次
    public void checkHeartbeat() {
        long currentTime = System.currentTimeMillis();
        ChatController.webSocketSet.forEach((key,value) ->{
            WebSocketSession webSocketSession = (WebSocketSession) value;
            Long lastHeartbeatTime = (Long) webSocketSession.getAttributes().get("lastHeartbeatTime");
            if (lastHeartbeatTime == null || currentTime - lastHeartbeatTime > 10000) { // 超过10秒未收到心跳消息
                try {
                    //取出
                    ChatController.webSocketSet.remove(key,value);
                    log.info("[websocket消息]:用户"+key+"心跳连接失败，强制下线");
                    webSocketSession.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
