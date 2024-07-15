package com.HelpMe.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatMessageForm {
    private Long publishId; // 发布者ID
    private String sender; // 发送者的标识，比如用户名
    private String message; // 消息内容
    private LocalDateTime timestamp; // 消息发送的时间戳
    private String messageType; // 消息类型，比如"text"、"image"等
    private Long targetId; // 目标用户ID，用于私聊；如果为null或特定值，表示群发或广播
}
