package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.model.OrderEntity;
import com.viettridao.cafe.repository.OrderRepository;
import com.viettridao.cafe.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    public List<OrderEntity> getOrdersByTable(Integer tableId) {
        return orderRepository.findAllByTable_IdAndIsDeletedFalse(tableId);
    }

    @Transactional
    @Override
    public OrderEntity createOrUpdateOrder(OrderEntity order) {
        order.setIsDeleted(false);
        return orderRepository.save(order);
    }
}
