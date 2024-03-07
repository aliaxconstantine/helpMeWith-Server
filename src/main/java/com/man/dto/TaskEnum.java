package com.man.dto;

public enum TaskEnum {
     TRUE(1,"承接"),
    FINISH(2,"承接，已完成"), UNFINISH(3,"承接，未完成"),
    PUBLISH(4,"发布"),
    FALSE(5,",发布，未承接"),PFINISH(6,"发布，承接，未完成"),PUNFILSH(7,"发布，完成");


     TaskEnum(Integer state, String desc){
        this.state = state;
         this.desc = desc;
     }
     public final Integer state;
     public final String desc;
}
