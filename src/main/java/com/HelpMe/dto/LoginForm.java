package com.HelpMe.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginForm {
    private String phone;
    private String code;
    private String password;
}
