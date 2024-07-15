package com.HelpMe.Controller;

import com.HelpMe.dto.ErrorCodeEnum;
import com.HelpMe.dto.HttpResult;
import com.HelpMe.Service.CoreService.AdvertisementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//广告控制器
@RestController
@ResponseBody
@RequestMapping(value = "/adv",produces = {"application/json;charset=utf-8"})
public class AdvertisementController {
    private final AdvertisementsService advertisementsService;

    @Autowired
    public AdvertisementController(AdvertisementsService advertisementsService) {
        this.advertisementsService = advertisementsService;
    }
    @GetMapping("/")
    public HttpResult getAdvertise(@RequestParam String advId) {
        return HttpResult.builder().code(ErrorCodeEnum.SUCCESS.code).data(advertisementsService.getById(advId)).build();
    }

    @GetMapping("/adv")
    public HttpResult getAdvertise() {
        return HttpResult.builder().code(ErrorCodeEnum.SUCCESS.code).msg("访问成功").data("访问成功").build();
    }
}

