package com.viettridao.cafe.service;

import com.viettridao.cafe.model.OrderEntity;
import com.viettridao.cafe.model.PaymentEntity;
import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.repository.OrderRepository;
import com.viettridao.cafe.repository.PaymentRepository;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.impl.PaymentServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private TableRepository tableRepository;
    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindUnpaidOrderByTableId_Found() {
        OrderEntity order = new OrderEntity();
        when(orderRepository.findFirstByTable_IdAndIsDeletedFalse(1)).thenReturn(Optional.of(order));
        Optional<OrderEntity> result = paymentService.findUnpaidOrderByTableId(1);
        assertTrue(result.isPresent());
        assertEquals(order, result.get());
    }

    @Test
    void testFindUnpaidOrderByTableId_NotFound() {
        when(orderRepository.findFirstByTable_IdAndIsDeletedFalse(1)).thenReturn(Optional.empty());
        Optional<OrderEntity> result = paymentService.findUnpaidOrderByTableId(1);
        assertFalse(result.isPresent());
    }

    @Test
    void testCalculateOrderTotal() {
        OrderEntity order = new OrderEntity();
        var detail = new com.viettridao.cafe.model.OrderDetailEntity();
        detail.setQuantity(2);
        // Giả lập giá món tại thời điểm order
        detail.setMenuItem(new com.viettridao.cafe.model.MenuItemEntity());
        detail.getMenuItem().setCurrentPrice(50000.0);
        order.setOrderDetails(java.util.List.of(detail));
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        long total = paymentService.calculateOrderTotal(1);
        assertEquals(100000L, total);
    }

    @Test
    void testProcessPayment() {
        OrderEntity order = new OrderEntity();
        order.setId(1);
        order.setOrderDetails(Collections.emptyList());
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(paymentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        paymentService.processPayment(order, 100000L, "note");
        verify(paymentRepository).save(any(PaymentEntity.class));
        verify(orderRepository).save(order);
        assertTrue(order.getIsDeleted());
    }

    @Test
    void testUpdateOrderAndTableStatusAfterPayment() {
        TableEntity table = new TableEntity();
        table.setStatus(com.viettridao.cafe.common.TableStatus.OCCUPIED);
        when(tableRepository.findById(1)).thenReturn(Optional.of(table));
        paymentService.updateOrderAndTableStatusAfterPayment(new OrderEntity(), 1);
        assertEquals(com.viettridao.cafe.common.TableStatus.AVAILABLE, table.getStatus());
        verify(tableRepository).save(table);
    }
}
