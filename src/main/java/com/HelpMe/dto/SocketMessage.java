package com.HelpMe.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SocketMessage {
    private Long sendId;
    private Long publishId;
    private String message;
}
