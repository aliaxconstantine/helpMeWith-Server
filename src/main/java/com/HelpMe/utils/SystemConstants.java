package com.HelpMe.utils;

/**
 * @author 艾莉希雅
 */
public class SystemConstants {
    public static final int SAVE_MESSAGE_TIME = 1000*(60*60*24);
    public static final String USER_NICK_NAME_PREFIX = "user_";
    public static final int DEFAULT_PAGE_SIZE = 5;
    public static final int MAX_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE_MESSAGE = 15;
    //TODO:设定静态资源上传地址
    public static final String IMAGE_UPLOAD_DIR = "/www/wwwroot/maskmanger/static/file";
    //不需要鉴权的url
    public static final String[] publicUrls = {"/data/**","/user/register","/v3/api-docs","/user/code",
            "/phoneLogin", "/userLogin","/swagger-ui.html","/task/tasks",
            "/swagger-ui/index.html","/user/pwdCode","/Token/token","/websocket/**"};
}
