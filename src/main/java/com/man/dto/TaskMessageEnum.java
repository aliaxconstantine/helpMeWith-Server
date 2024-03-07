package com.man.dto;

public enum TaskMessageEnum {
    TRUE("显示", 1),FALSE("隐藏", 2);
    TaskMessageEnum(String state, Integer code){
        this.state = state;
        this.code = code;
    }
    public final String state;
    public final Integer code;
}
