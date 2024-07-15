package com.HelpMe.Service.CoreService;

import com.HelpMe.dto.HttpResult;
import com.HelpMe.Entity.core.TOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 艾莉希雅
* @description 针对表【orders】的数据库操作Service
* @createDate 2023-10-14 17:53:56
*/
public interface OrdersService extends IService<TOrder> {

    HttpResult processPayment(String orders);

    HttpResult unPayment(String orderId);

    HttpResult confirmRefund(String refundId);

    HttpResult getOrders(String orderId);

    HttpResult getOrdersByTask(String taskID);

    HttpResult getUnPayment(String orderId);
}
