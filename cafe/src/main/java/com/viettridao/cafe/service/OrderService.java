package com.viettridao.cafe.service;

import com.viettridao.cafe.model.OrderEntity;
import java.util.List;

public interface OrderService {
    List<OrderEntity> getOrdersByTable(Integer tableId);

    OrderEntity createOrUpdateOrder(OrderEntity order);
}
