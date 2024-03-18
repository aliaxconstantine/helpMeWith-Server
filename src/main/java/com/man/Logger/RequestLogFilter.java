package com.man.Logger;

import com.man.utils.RequestWrapper;
import com.man.utils.ResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Enumeration;
/**
 * @author 艾莉希雅
 * &#064;Description  请求日志拦截器
 * &#064;Version  1.0
 **/
@Log4j2
@Component
public class RequestLogFilter implements HandlerInterceptor {
    Long startTime;
    Long endTime;
    /**
    *  &#064;Description  请求日志拦截器
    *  &#064;Version  1.0
     **/
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        // 记录请求信息
        startTime = System.currentTimeMillis();
        ResponseUtils.CORS(response);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
        endTime = System.currentTimeMillis();
        Long time = endTime - startTime;
        String method = request.getMethod();
        String url = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        //如果queryString为空
        String headers = getRequestHeaders(request);
        RequestWrapper cachingRequestWrapper = new RequestWrapper(request);
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            queryString = cachingRequestWrapper.getBody();
        }

        log.info("[接口日志]：请求头：[{}]，请求IP：[{}]，请求设备：[{}]，请求描述：[{}]，请求用户：[{}]，请求时间[{}]",
                headers, request.getRemoteAddr(), request.getHeader("User-Agent"), url, request.getHeader("X-Forwarded-For"),time);
        if(time > 1000){
            log.error("[接口日志]请求超时，请求时间[{}]，请求头[{}]",time,url);
        }
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
