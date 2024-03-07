package com.man.controller;

import com.man.dto.ErrorCodeEnum;
import com.man.dto.HttpResult;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping(produces = {"application/json;charset=utf-8"})
//异常处理器
public class ExceptionController implements ErrorController {
    @ResponseBody
    @GetMapping("/error")
    @ExceptionHandler(Exception.class)
    public HttpResult handleRuntimeException(Exception ex) {
        //TODO:将调用堆栈放入日志文件
        return HttpResult.builder().code(ErrorCodeEnum.FAIL.code).msg(HttpStatus.INTERNAL_SERVER_ERROR +"错误为:"+ex.getMessage())
                .build();
    }
    @ResponseBody
    @ExceptionHandler(RedisConnectionFailureException.class)
    public ResponseEntity<?> handleRedisConnectionFailureException(RedisConnectionFailureException ex) {
        log.error(ex.getMessage());
        String errorMessage = "无法连接到 Redis";
        String jsonError = "{\"error\": \"" + errorMessage + "\"}";
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(jsonError);
    }
    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.error(ex.getMessage());
        String errorMessage = "不支持该 HTTP 方法";
        String jsonError = "{\"error\": \"" + errorMessage + "\"}";
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(jsonError);
    }
    public static String getExceptionAllinFormation(Exception ex){
        String sOut = "";
        StackTraceElement[] trace = ex.getStackTrace();
        for (StackTraceElement s : trace) {
            sOut += "\tat " + s + "\r\n";
        }
        return sOut;
    }
}

