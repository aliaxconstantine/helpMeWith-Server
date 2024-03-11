package com.man.Logger;

import com.man.utils.RequestWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Enumeration;

@Log4j2
@Component
public class RequestLogFilter implements HandlerInterceptor {
    Long startTime;
    Long endTime;
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        // 记录请求信息
        startTime = System.currentTimeMillis();
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
        endTime = System.currentTimeMillis();
        Long time = endTime - startTime;
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        //如果是post或者queryString为空
        if(method.equals("POST") && queryString == null){
            queryString = request.getInputStream().toString();
        }
        //如果不是
        if(!method.equals("POST") && queryString == null){
            queryString = request.getPathInfo();
        }
        String headers = getRequestHeaders(request);
        RequestWrapper cachingRequestWrapper = new RequestWrapper(request);

        if ("POST".equalsIgnoreCase(request.getMethod())) {
            queryString = cachingRequestWrapper.getBody();
        }
        log.info("收到 {} 请求，URL：[{}]，参数：[{}]，请求头：[{}]，请求时间[{}]", method, url, queryString, headers,time);
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    private String getRequestHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuilder headers = new StringBuilder();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            headers.append(name).append(":").append(value).append(",");
        }
        if (headers.length() > 0) {
            headers.setLength(headers.length() - 1);
        }
        return headers.toString();
    }
}
