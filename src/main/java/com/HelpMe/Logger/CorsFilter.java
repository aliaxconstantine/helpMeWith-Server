package com.HelpMe.Logger;

import com.HelpMe.utils.ResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
/**
 * @author 艾莉希雅
 * @Description: CORS处理器
 * @Version: 1.0
 */
@Component
@Log4j2
public class CorsFilter implements HandlerInterceptor {
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, HttpServletResponse response, @NotNull Object handler) throws Exception {
        // 允许所有来源访问
        ResponseUtils.CORS(response);
        return true;
    }
}
