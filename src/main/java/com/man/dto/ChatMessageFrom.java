package com.man.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessageFrom {
    private String message;
    private Long id;
}
