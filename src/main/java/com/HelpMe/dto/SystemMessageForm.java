package com.HelpMe.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SystemMessageForm {
    private String userId;
    private String message;
}
