package com.HelpMe.Controller;

import com.HelpMe.dto.HttpResult;
import com.HelpMe.Service.CoreService.SysMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/smsg")
public class SysMessageController {
    private final SysMessageService sysMessageService;
    @Autowired
    public SysMessageController(SysMessageService sysMessageService) {
        this.sysMessageService = sysMessageService;
    }

    @GetMapping("/all")
    public HttpResult getAllSystemMessage(@RequestParam(name = "pageNum") Long pageNum){
        return sysMessageService.getAllMessage(pageNum);
    }

}
