package com.man.controller;
import com.man.dto.ErrorCodeEnum;
import com.man.dto.HttpResult;
import com.man.dto.TaskForm;
import com.man.entity.core.Task;
import com.man.entity.core.TaskMessage;
import com.man.service.CoreService.*;

import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@ResponseBody
@RestController
@Log4j2
@RequestMapping(value = "/task",produces = {"application/json;charset=utf-8"})
public class TaskController {
    private final TasksService tasksService;
    private final TaskMessageService taskMessageService;
    private final OrdersService ordersService;
    private final TaskCategoriyService taskCategoriyService;
    private final TaskTimesService tasktimesService;
    private final UserLikeMessageService userLikeMessageService;

    @Autowired
    public TaskController(TasksService tasksService, TaskMessageService taskMessageService, OrdersService ordersService, TaskCategoriyService taskCategoriyService, TaskTimesService tasktimesService, UserLikeMessageService userLikeMessageService) {
        this.tasksService = tasksService;
        this.taskMessageService = taskMessageService;
        this.ordersService = ordersService;
        this.taskCategoriyService = taskCategoriyService;
        this.tasktimesService = tasktimesService;
        this.userLikeMessageService = userLikeMessageService;
    }

    //根据id获取任务信息
    @ResponseBody
    @GetMapping("/{id}")
    public HttpResult queryTaskById(@PathVariable("id") Long id) {
        return tasksService.queryTaskById(id);
    }
    //根据id获取任务时间
    @ResponseBody
    @GetMapping("/time")
    public HttpResult getTime(@RequestParam("taskId") Long taskId){
        return tasksService.getTaskTime(taskId);
    }

    // 发布任务
    @ResponseBody
    @PostMapping("/send")
    public HttpResult publishTask(@Validated @RequestBody TaskForm task) {
        taskCategoriyService.submitNew(task);
        return tasksService.createTask(task);
    }

    //修改任务属性
    @ResponseBody
    @PostMapping("/upload")
    public HttpResult uploadTask(@Validated @RequestBody TaskForm task) {
        //实现更新任务的逻辑
        tasksService.update(Task.builder()
                .name(task.getName())
                .description(task.getDescription())
                .x(task.getX())
                .y(task.getY())
                .type(String.join(",",task.getType()))
                .imageUrl(String.join("",task.getImageUrl()))
                .price(task.getPrice())
                .build());
        taskCategoriyService.submitNew(task);
        return HttpResult.builder().code(ErrorCodeEnum.SUCCESS.code).msg("修改成功").build();
    }
    //获取任务下的所有对话
    @ResponseBody
    @GetMapping("/{taskId}/chat")
    public HttpResult getChat(@PathVariable String taskId){
       return taskMessageService.getTaskChat(taskId);
    }

    // 添加对话到任务下
    @ResponseBody
    @PostMapping("/{taskId}/conversation")
    public HttpResult addConversationToTask(@PathVariable("taskId") Long taskId, @Validated @RequestBody TaskMessage taskMessage) {
        //根据 taskId 将 conversation 添加到对应的任务下
        boolean isTrue = taskMessageService.save(taskMessage);
        if (!isTrue) {
            return HttpResult.builder().code(ErrorCodeEnum.FAIL.code).msg("评论失败").build();
        }
        //在 task 下新增任务数
        return tasksService.addMessageCount(taskId);
    }

    @DeleteMapping("/conversation/{conversationId}")
    public HttpResult deleteConversationFromTask(@PathVariable("conversationId") Long conversationId) {
        // 根据 taskId 和 conversationId 删除对应任务下的对话
        return taskMessageService.updateState(conversationId, false);
    }

    @ResponseBody
    @DeleteMapping("/{taskId}")
    public HttpResult deleteTask(@PathVariable("taskId") String taskId) {
        // 根据 taskId 删除对应的任务
        boolean ifTure = tasksService.removeById(taskId);
        if (!ifTure) {
            return HttpResult.builder().code(ErrorCodeEnum.FAIL.code).msg("删除失败").build();
        }
        return HttpResult.builder().code(ErrorCodeEnum.SUCCESS.code).data(true).msg("删除成功").build();
    }

    //获取位置的任务
    @Validated
    @GetMapping("/tasks")
    @ResponseBody
    public HttpResult getTasksByType(
             @NotNull @RequestParam(name = "x",required = false) Double x,
             @NotNull @RequestParam(name = "y",required = false) Double y,
             @NotNull @RequestParam(name = "pageNum",required = false) Integer pageNum) {
        if (x != null && y!= null) {
            // 按照指定属性排序
            return tasksService.getTasksByLocation(pageNum, x, y);
        } else {
            // 默认按照发布时间排序
            return tasksService.getTasksByTime(pageNum);
        }
    }

    @GetMapping("/key")
    @ResponseBody
    public HttpResult getByKey(@RequestParam(name = "taskType") String enkey ,
                                @RequestParam(name = "pageNum") Integer pageNum) {
       return tasksService.getByKey(enkey,pageNum);
    }

    @GetMapping("/type")
    @ResponseBody
    public HttpResult getTypeToTask(
            @RequestParam(name="type") String type,
            @RequestParam(name="pageNum")Integer pageNum
    ){
        return tasksService.getTypeTasks(type,pageNum);
    }


    @PostMapping("/{taskId}/accept")
    public HttpResult acceptTask(@PathVariable(name = "taskId") Long taskId) {
        return tasksService.acceptTask(taskId);
    }

    @PostMapping("/progress")
    public HttpResult submitTaskProgress(@RequestParam(name = "taskId") Long taskId,
                                         @RequestParam(name = "progress") String progress) {
        return tasksService.submitTaskProgress(taskId, progress);
    }

    @PostMapping("/tasks/confirm")
    public HttpResult confirmTaskCompletion(@RequestParam(name = "taskId") Long taskId) {
        return tasksService.confirmTaskCompletion(taskId);
    }
    //支付
    @PostMapping("/pay")
    public HttpResult handlePayment(@RequestBody String orderId) {
        return ordersService.processPayment(orderId);
    }

    //取消支付
    @PostMapping("/unPay")
    public HttpResult unPayment(@RequestParam String orderId) {
        return ordersService.unPayment(orderId);
    }

    @PostMapping("/confirmRefund")
    public HttpResult confirmRefund(@RequestParam String refundId) {
        return ordersService.confirmRefund(refundId);
    }
}