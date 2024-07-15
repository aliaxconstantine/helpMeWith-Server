package com.HelpMe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HttpResult {
    private Integer code; //响应码
    private String msg; //消息
    private Object data; //数据
    //常见地返回格式
    public static HttpResult success(Object data) {
        return HttpResult.builder()
                .code(ErrorCodeEnum.SUCCESS.code)
                .data(data)
                .msg(ErrorCodeEnum.SUCCESS.data)
                .build();
    }
    public static HttpResult success(String data) {
        return HttpResult.builder()
                .code(ErrorCodeEnum.SUCCESS.code)
                .msg(data)
                .build();
    }
    public static HttpResult fail(String message) {
        return HttpResult.builder()
                .code(ErrorCodeEnum.FAIL.code)
                .msg(message)
                .build();
    }

}
