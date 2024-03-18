package com.man.config.secuityconfig.secuityEvent;

import com.man.dto.ErrorCodeEnum;
import com.man.dto.HttpResult;
import com.man.utils.JsonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * CAuthenticationFailureHandler是一个认证失败处理器，用于处理认证失败的情况。
 *
 * @author 艾莉希雅
 * @note 该类实现了Spring Security框架中的AuthenticationFailureHandler接口，
 * 当用户认证失败时，系统会调用该类的onAuthenticationFailure方法进行相关处理。
 */
@Component
public class CAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 创建要写入响应输出流的 HttpResult 对象
        HttpResult result = HttpResult.builder().code(ErrorCodeEnum.FAIL.code)
                .msg("错误原因: " + exception.getMessage())
                .build();
        JsonUtil.printToJson(result,response);
    }
}
