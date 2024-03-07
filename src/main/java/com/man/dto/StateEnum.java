package com.man.dto;

public enum StateEnum {
    UN_PUBLISH(2,"未承接"),
    PUBLISH_FALSE(1,"已承接，未完成"),
    PUBLISH_TRUE(3,"承接，完成");

    public final Integer state;
    public final String desc;
    StateEnum(Integer state, String desc){
         this.state = state;
         this.desc = desc;
    }

}
