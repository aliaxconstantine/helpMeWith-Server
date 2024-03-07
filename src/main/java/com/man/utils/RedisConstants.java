package com.man.utils;

public class RedisConstants {
    public static final String LOGIN_CODE_KEY = "login:code:";
    public static final Long LOGIN_CODE_TTL = 2L;
    public static final String LOGIN_USER_KEY = "login:token:";
    public static final Long LOGIN_USER_TTL = 30L;
    public static final String CACHE_TASK_KEY = "cache:task";

    public static final Long CACHE_TASK_TTL = 5L;
    public static final String LOCK_TASK_KEY = "lock:task:";
    public static final Long LOCK_TASK_TTL = 30L;
    public static final Long CACHE_NULL_TTL = 30L;
    public static final String TASK_GEO_KEY = "geo:task";
    public static final String MESSAGE_KEY = "message:like";
    public static final Long Message_TTL = 5L;
    public static final String SYSMESSAGE_KEY = "message:like";
    public static final Long SYSMessage_TTL = 5L;
}
