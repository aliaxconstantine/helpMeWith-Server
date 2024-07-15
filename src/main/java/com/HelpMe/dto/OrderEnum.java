package com.HelpMe.dto;

public enum OrderEnum {

    OUT("未支付"),
    PAID("已支付"),
    CANCELLED("已取消"),

    COMPLETED("已完成"),
    UNREFUNDED("未退款"),
    REFUNDED("已退款");
    public String status;
    OrderEnum(String status){
        this.status = status;
    }
}
