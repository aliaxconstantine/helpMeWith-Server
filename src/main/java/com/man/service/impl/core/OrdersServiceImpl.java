package com.man.service.impl.core;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.man.dto.*;
import com.man.entity.core.TOrder;
import com.man.entity.core.RefundRecord;
import com.man.entity.core.Task;
import com.man.mapper.OrdersMapper;
import com.man.mapper.TasksMapper;
import com.man.service.CoreService.OrdersService;
import com.man.service.CoreService.RefundRecordService;
import com.man.utils.AuthenticationUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
* @author 艾莉希雅
* @description 针对表【orders】的数据库操作Service实现
* @createDate 2023-10-14 17:53:56
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, TOrder> implements OrdersService {

    private final RabbitTemplate rabbitTemplate;
    private final RefundRecordService refundRecordService;
    private final TasksMapper tasksMapper;


    @Autowired
    public OrdersServiceImpl(RabbitTemplate rabbitTemplate, RefundRecordService refundRecordService, TasksMapper tasksMapper){
        this.rabbitTemplate = rabbitTemplate;
        this.refundRecordService = refundRecordService;
        this.tasksMapper = tasksMapper;
    }
    //用户已支付
    @Override
    @Transactional
    public HttpResult processPayment(String orderId) {
        // 根据订单号查询订单信息
        TOrder order = getById(orderId);
        if (order == null) {
            return HttpResult.builder().data(false).msg("订单不存在").code(ErrorCodeEnum.FAIL.code).build();
        }
        // 更新订单状态为已支付
        order.setStatus(OrderEnum.PAID.status);
        boolean ifTrue = update().update(order);
        if(!ifTrue){
            throw new RuntimeException("服务器异常");
        }
        // 使用消息队列处理 其他业务逻辑、发送通知等
        Task task = Task.builder().id(order.getCustomerId()).status(TaskEnum.PAYFINISH.state).build();
        //修改任务状态为已经支付
        rabbitTemplate.convertAndSend(RabbitMessage.EXCHANGE_NAME,RabbitMessage.ORDER_UPDATE_ROUTING_KEY,
                JSONUtil.toJsonStr(task));

        task = tasksMapper.selectById(order.getCustomerId());
        //向客户发送消息
        rabbitTemplate.convertAndSend(RabbitMessage.EXCHANGE_NAME, RabbitMessage.SYSTEM_INFO_ROUTING_KEY,
              JSONUtil.toJsonStr(
                      SystemMessageForm.builder().message("您承接的任务"+task.getName()+"任务发起方已付款").userId(task.getAssigneeId().toString()).build()
              ));
        rabbitTemplate.convertAndSend(RabbitMessage.EXCHANGE_NAME, RabbitMessage.SYSTEM_INFO_ROUTING_KEY,
                JSONUtil.toJsonStr(
                        SystemMessageForm.builder().message("您的任务"+task.getName()+"已付款").userId(AuthenticationUtils.getId().toString())
                ));
        // 返回支付成功的响应
        return HttpResult.builder().data(true).msg("支付成功").code(ErrorCodeEnum.SUCCESS.code).build();
    }

    // 退款申请
    @Transactional
    public HttpResult unPayment(String orderId) {
        // 根据订单编号查询订单信息
        TOrder order = getById(orderId);
        if (order == null) {
            // 如果订单不存在，返回错误提示
            return HttpResult.fail("订单不存在");
        }
        RefundRecord refundRecordQuery = refundRecordService.getOne(new QueryWrapper<RefundRecord>().eq("order_id", orderId));
        //如果已经有退款单，则使用该退款单
        if(refundRecordQuery != null){
            return HttpResult.success(refundRecordQuery);
        }
        // 如果订单状态不允许退款（例如已经退款、已经发货），则拒绝该退款请求
        if (!Objects.equals(order.getStatus(), OrderEnum.PAID.status)) {
            return HttpResult.fail("订单已经退款");
        }
        // 将订单状态设置为“待退款”
        order.setStatus(OrderEnum.UNREFUNDED.status);
        update().update(order);
        // 执行一些后续操作，例如向用户发送退款通知等
        RefundRecord refundRecord = RefundRecord.builder()
                .orderId(order.getId())
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .status(RefundStatus.REFUNDING)
                .amount(order.getUnitPrice())
                .build();
        refundRecordService.save(refundRecord);
        return HttpResult.success(refundRecord);
    }
    @Transactional
    // 确认退款
    public HttpResult confirmRefund(String refundId) {
        // 根据退款记录编号查询退款记录信息
        RefundRecord refundRecord = refundRecordService.getById(refundId);
        if (refundRecord == null) {
            // 如果退款记录不存在，返回错误提示
            return HttpResult.fail("退款单不存在");
        }
        // 如果退款记录状态不为“退款中”，返回错误提示
        if (!Objects.equals(refundRecord.getStatus(), RefundStatus.REFUNDING)) {
            return HttpResult.fail("并未发起退款");
        }
        // 执行退款操作，例如调用支付系统的退款接口
        // 更新订单状态为“已退款”
        TOrder order = getById(refundRecord.getOrderId());
        order.setStatus(OrderEnum.REFUNDED.status);
        boolean ifUpTrue = update().eq("id", refundRecord.getOrderId()).update(order);
        if(!ifUpTrue){
            throw new RuntimeException("服务器异常");
        }

        // 更新退款记录状态为“已确认”
        refundRecord.setStatus(RefundStatus.CONFIRMED);
        ifUpTrue = refundRecordService.update().eq("order_id", refundRecord.getOrderId()).update(refundRecord);
        if(!ifUpTrue){
            throw new RuntimeException("服务器异常");
        }
        // 执行一些后续操作，例如向用户发送退款成功通知等
        //将任务状态改为已退款
        Task task = Task.builder().id(order.getCustomerId()).status(TaskEnum.UNPAYFINISH.state).build();
        rabbitTemplate.convertAndSend(RabbitMessage.EXCHANGE_NAME,RabbitMessage.ORDER_UPDATE_ROUTING_KEY, JSONUtil.toJsonStr(task));
        //向用户发送退款成功通知
        task = tasksMapper.selectById(order.getCustomerId());
        rabbitTemplate.convertAndSend(RabbitMessage.EXCHANGE_NAME, RabbitMessage.SYSTEM_INFO_ROUTING_KEY,
                JSONUtil.toJsonStr(
                SystemMessageForm.builder()
                        .message("任务"+task.getName()+"任务承接者已退款")
                        .userId(task.getInitiatorId().toString())));
        rabbitTemplate.convertAndSend(RabbitMessage.EXCHANGE_NAME, RabbitMessage.SYSTEM_INFO_ROUTING_KEY,
                JSONUtil.toJsonStr(
                SystemMessageForm.builder()
                        .message("您的任务"+task.getName()+"退款成功")
                        .userId(task.getAssigneeId().toString())));
        return HttpResult.success(refundRecord);
    }

    @Override
    public HttpResult getOrders(String orderId) {
        TOrder tOrder = getById(orderId);
        if(tOrder == null){
           return HttpResult.fail("订单id错误");
        }
        return HttpResult.success(tOrder);
    }

    @Override
    public HttpResult getOrdersByTask(String taskID) {
        //根据id获取到订单
        List<TOrder> orders = query().eq("customer_id", taskID).list().stream().toList();
        if (orders.size() < 1) {
            return HttpResult.fail("订单id错误");
        }
        else {
            return HttpResult.success(orders.get(0));
        }
    }

    @Override
    public HttpResult getUnPayment(String orderId) {
        //获取有没有退款单
        RefundRecord tOrder = refundRecordService.query().eq("order_id", orderId).one();
        return HttpResult.success(tOrder);
    }
}




