package com.man.dto;

import lombok.Builder;
import lombok.Data;

public enum ErrorCodeEnum {
    SUCCESS(0,"请求成功"),
    FAIL(1,"请求失败");
    public final Integer code;
    public final String data;

    ErrorCodeEnum(Integer code, String data) {
        this.code = code;
        this.data = data;
    }
    @Override
    public String toString() {
        return "错误码："+code+"，错误信息："+data;
    }
}
