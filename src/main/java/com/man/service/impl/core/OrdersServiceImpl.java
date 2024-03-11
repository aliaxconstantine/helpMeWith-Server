package com.man.service.impl.core;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.man.dto.*;
import com.man.entity.core.TOrder;
import com.man.entity.core.RefundRecord;
import com.man.mapper.OrdersMapper;
import com.man.service.CoreService.OrdersService;
import com.man.service.CoreService.RefundRecordService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
    @Autowired
    public OrdersServiceImpl(RabbitTemplate rabbitTemplate, RefundRecordService refundRecordService){
        this.rabbitTemplate = rabbitTemplate;
        this.refundRecordService = refundRecordService;
    }
    //用户已支付
    @Override
    public HttpResult processPayment(String orderId) {
        // 根据订单号查询订单信息
        TOrder order = getById(orderId);
        if (order == null) {
            return HttpResult.builder().msg("订单不存在").code(ErrorCodeEnum.FAIL.code).build();
        }
        // 更新订单状态为已支付
        order.setStatus(OrderEnum.PAID.status);
        boolean ifTrue = update().update(order);
        if(!ifTrue){
            throw new RuntimeException("服务器异常");
        }
        // 使用消息队列处理 其他业务逻辑、发送通知等
        //修改任务状态
        rabbitTemplate.convertAndSend(RabbitMessage.EXCHANGE_NAME,RabbitMessage.ORDER_UPDATE_ROUTING_KEY,order.getProductId());
        // 返回支付成功的响应
        return HttpResult.builder().data(true).msg("支付成功").code(ErrorCodeEnum.SUCCESS.code).build();
    }

    // 退款申请
    public HttpResult unPayment(String orderId) {
        // 根据订单编号查询订单信息
        TOrder order = getById(orderId);
        if (order == null) {
            // 如果订单不存在，返回错误提示
            return HttpResult.fail("订单不存在");
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
        refundRecordService.update().update(refundRecord);
        return HttpResult.success(refundRecord);
    }

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
        boolean ifUpTrue = update().update(order);
        if(!ifUpTrue){
            throw new RuntimeException("服务器异常");
        }

        // 更新退款记录状态为“已确认”
        refundRecord.setStatus(RefundStatus.CONFIRMED);
        ifUpTrue = refundRecordService.update().update(refundRecord);
        if(!ifUpTrue){
            throw new RuntimeException("服务器异常");
        }
        // 执行一些后续操作，例如向用户发送退款成功通知等

        return HttpResult.success(refundRecord);
    }

    @Override
    public HttpResult getOrders(String orderId) {
        TOrder tOrder = getById(orderId);
        if(tOrder == null){
           return HttpResult.fail("错误的id");
        }
        return HttpResult.success(tOrder);
    }
}




