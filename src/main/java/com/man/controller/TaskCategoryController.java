package com.man.controller;

import com.man.dto.HttpResult;
import com.man.service.CoreService.TaskCategoriyService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@ResponseBody
@RestController
@Log4j2
@RequestMapping(value = "/taskCategory",produces = {"application/json;charset=utf-8"})
public class TaskCategoryController {
    private final TaskCategoriyService taskCategoryService;

    public TaskCategoryController(TaskCategoriyService taskCategoryService) {
        this.taskCategoryService = taskCategoryService;
    }
    @GetMapping("/all")
    public HttpResult getAllCategory(){
        return taskCategoryService.getAllCate();
    }

}
