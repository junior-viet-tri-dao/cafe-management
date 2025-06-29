package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.model.PaymentEntity;
import com.viettridao.cafe.model.OrderEntity;
import com.viettridao.cafe.repository.PaymentRepository;
import com.viettridao.cafe.repository.OrderRepository;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final TableRepository tableRepository;

    @Transactional
    @Override
    public PaymentEntity createPayment(PaymentEntity payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Optional<OrderEntity> findUnpaidOrderByTableId(Integer tableId) {
        // Lấy order đầu tiên chưa xóa của bàn (giả sử là order chưa thanh toán)
        return orderRepository.findFirstByTable_IdAndIsDeletedFalse(tableId);
    }

    @Override
    public long calculateOrderTotal(Integer orderId) {
        Optional<OrderEntity> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty())
            return 0L;
        OrderEntity order = orderOpt.get();
        // Tính tổng tiền từ orderDetails
        return order.getOrderDetails().stream()
                .mapToLong(od -> od.getQuantity() * (od.getPrice() != null ? od.getPrice().longValue() : 0L))
                .sum();
    }

    @Transactional
    @Override
    public void processPayment(OrderEntity order, Long customerPaid, String note) {
        // Lưu PaymentEntity
        PaymentEntity payment = new PaymentEntity();
        payment.setOrder(order);
        payment.setCustomerPaid(customerPaid);
        payment.setAmount(calculateOrderTotal(order.getId()));
        payment.setChangeAmount(customerPaid - payment.getAmount());
        payment.setNote(note);
        paymentRepository.save(payment);
        // Đánh dấu order đã thanh toán (isDeleted = true)
        order.setIsDeleted(true);
        orderRepository.save(order);
    }

    @Transactional
    @Override
    public void updateOrderAndTableStatusAfterPayment(OrderEntity order, Integer tableId) {
        // Cập nhật trạng thái bàn nếu cần
        var tableOpt = tableRepository.findById(tableId);
        tableOpt.ifPresent(table -> {
            table.setStatus(com.viettridao.cafe.common.TableStatus.AVAILABLE);
            tableRepository.save(table);
        });
    }
}
