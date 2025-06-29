
package com.viettridao.cafe.service;

import com.viettridao.cafe.model.PaymentEntity;
import com.viettridao.cafe.model.OrderEntity;
import java.util.Optional;

public interface PaymentService {
    PaymentEntity createPayment(PaymentEntity payment);

    // Lấy order chưa thanh toán của bàn
    Optional<OrderEntity> findUnpaidOrderByTableId(Integer tableId);

    // Tính tổng tiền của order
    long calculateOrderTotal(Integer orderId);

    // Xử lý thanh toán: lưu PaymentEntity, cập nhật trạng thái order
    void processPayment(OrderEntity order, Long customerPaid, String note);

    // Cập nhật trạng thái order và bàn sau khi thanh toán
    void updateOrderAndTableStatusAfterPayment(OrderEntity order, Integer tableId);
}
