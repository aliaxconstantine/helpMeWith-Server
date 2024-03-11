package com.man.Exception;

import com.man.dto.ErrorCodeEnum;
import com.man.dto.HttpResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping(produces = {"application/json;charset=utf-8"})
//异常处理器
public class ExceptionController implements ErrorController {
    private final String ERROR_PATH = "/error";



    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public HttpResult handleNotFoundException(ChangeSetPersister.NotFoundException ex) {
        return HttpResult.fail("找不到该页面");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public HttpResult handleAccessDeniedException(AccessDeniedException ex) {
        return HttpResult.fail("没有访问权限");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public HttpResult handleGenericException(Exception ex) {
        return HttpResult.fail("服务器错误");
    }


    //参数校验错误检测
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public HttpResult methodArgumentNotValidException(MethodArgumentNotValidException e){
        StringBuffer sb = new StringBuffer();
        e.getBindingResult().getFieldErrors().forEach( x -> sb.append(x.getField()).append(x.getDefaultMessage()).append(","));
        String message = sb.toString();
        return HttpResult.fail(message.substring(0,message.length()-1));
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
