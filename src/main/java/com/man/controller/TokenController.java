package com.man.controller;

import com.man.dto.HttpResult;
import com.man.utils.JWTUtils;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/Token",produces = {"application/json;charset=utf-8"})
public class TokenController {
    @ResponseBody
    @PostMapping("/token")
    public HttpResult getToken(@RequestParam(name = "token") String token){
        Boolean flag = JWTUtils.verifyToken(token);
        return HttpResult.success(flag);
    }
}
