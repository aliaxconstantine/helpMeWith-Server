package com.man.utils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

public class SystemConstants {
    public static final String USER_NICK_NAME_PREFIX = "user_";
    public static final int DEFAULT_PAGE_SIZE = 5;
    public static final int MAX_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE_MESSAGE = 15;
    //TODO:设定静态资源上传地址
    public static final String IMAGE_UPLOAD_DIR = "D:\\code\\picture";
    //不需要鉴权的url
    public static final String[] publicUrls = {"/data/**","/user/register","/v3/api-docs","/user/code",
            "/phoneLogin", "/userLogin","/swagger-ui.html","/task/tasks",
            "/swagger-ui/index.html","/user/pwdCode","/Token/token","/websocket/**"};
}
