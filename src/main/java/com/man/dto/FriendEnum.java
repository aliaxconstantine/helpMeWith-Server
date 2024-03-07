package com.man.dto;

public enum FriendEnum {
    FRIEND(0,"好友"),BLACK(1,"黑名单");
    public final Integer state;
    public final String desc;
    FriendEnum(Integer state, String desc){
        this.state = state;
        this.desc = desc;
    }

    }
