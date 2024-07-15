package com.HelpMe.Controller;
import com.HelpMe.dto.ErrorCodeEnum;
import com.HelpMe.dto.HttpResult;
import com.HelpMe.dto.TaskForm;
import com.HelpMe.Entity.core.Task;
import com.HelpMe.Entity.core.TaskMessage;
import com.HelpMe.Service.CoreService.*;

import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author 艾莉希雅
 */
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
    private final TaskImagesService taskImagesService;

    @Autowired
    public TaskController(TasksService tasksService, TaskMessageService taskMessageService, OrdersService ordersService, TaskCategoriyService taskCategoriyService, TaskTimesService tasktimesService, UserLikeMessageService userLikeMessageService, TaskImagesService taskImagesService) {
        this.tasksService = tasksService;
        this.taskMessageService = taskMessageService;
        this.ordersService = ordersService;
        this.taskCategoriyService = taskCategoriyService;
        this.tasktimesService = tasktimesService;
        this.userLikeMessageService = userLikeMessageService;
        this.taskImagesService = taskImagesService;
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

    //发布任务
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
        taskMessage.setCreateTime(new Timestamp(System.currentTimeMillis()));
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
             @NotNull @RequestParam(name = "x" , required = false) Double x,
             @NotNull @RequestParam(name = "y" , required = false) Double y,
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

    //承接任务
    @PostMapping("/{taskId}/accept")
    public HttpResult acceptTask(@PathVariable(name = "taskId") Long taskId) {
        return tasksService.acceptTask(taskId);
    }

    //任务完成
    @PostMapping("/ok")
    public HttpResult okTask(@RequestParam(name = "taskId") Long taskId){
        return tasksService.successTask(taskId.toString());
    }

    //上传任务进度
    @PostMapping("/progress")
    public HttpResult submitTaskProgress(@RequestBody List<String> imagesList,@RequestParam(name="taskId") Long taskId) {
        return taskImagesService.submitTaskProgress(imagesList,taskId);
    }

    //获取任务进度
    @GetMapping("/tasks/progress")
    public HttpResult getTaskProgresses(@RequestParam(name = "taskId") Long taskId){
        return taskImagesService.getTaskProgresses(taskId);
    }
    //确认任务完成
    @PostMapping("/tasks/confirm")
    public HttpResult confirmTaskCompletion(@RequestParam(name = "taskId") Long taskId) {
        return tasksService.confirmTaskCompletion(taskId);
    }
    //获取订单
    @GetMapping("/pay")
    public HttpResult getOrders(@RequestParam String orderId){
        return ordersService.getOrders(orderId);
    }

    //获取该任务是否有订单
    @GetMapping("/order")
    public HttpResult getTaskOrders(@RequestParam String taskID){return ordersService.getOrdersByTask(taskID);}

    //支付
    @PostMapping("/pay")
    public HttpResult handlePayment(@RequestParam String orderId) {
        return ordersService.processPayment(orderId);
    }

    //获取退款单
    @GetMapping("/confirmRefund")
    public HttpResult getUnPayment(@RequestParam String orderId) {
        return ordersService.getUnPayment(orderId);
    }

    //退款
    @PostMapping("/unPay")
    public HttpResult unPayment(@RequestParam String orderId) {
        return ordersService.unPayment(orderId);
    }

    //确认退款
    @PostMapping("/confirmRefund")
    public HttpResult confirmRefund(@RequestParam String refundId) {
        return ordersService.confirmRefund(refundId);
    }
}