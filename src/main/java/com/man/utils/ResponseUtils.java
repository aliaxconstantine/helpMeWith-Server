package com.man.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
@Log4j2
public class ResponseUtils {
    public static HttpServletResponse CORS(HttpServletResponse response) throws Exception {
        // 允许所有来源访问
        log.error("已经允许跨域访问");
        response.setHeader("Access-Control-Allow-Origin", "*");
        // 允许请求的方法
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        // 允许请求的头部信息
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        // 预检请求的有效期，单位为秒
        response.setHeader("Access-Control-Max-Age", "3600");
        return response;
    }
}
