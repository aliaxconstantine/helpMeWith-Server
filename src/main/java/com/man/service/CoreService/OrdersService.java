package com.man.service.CoreService;

import com.man.dto.HttpResult;
import com.man.entity.core.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 艾莉希雅
* @description 针对表【orders】的数据库操作Service
* @createDate 2023-10-14 17:53:56
*/
public interface OrdersService extends IService<Orders> {

    HttpResult processPayment(String orders);

    HttpResult unPayment(String orderId);

    HttpResult confirmRefund(String refundId);
}
