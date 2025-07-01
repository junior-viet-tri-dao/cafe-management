package com.viettridao.cafe.dto.request.Pay;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {
    private Integer tableId;
    private Double customerCash; // Số tiền khách đưa
    private Boolean freeTable;   // Có chuyển bàn về trống sau khi thanh toán không
    private List<PaymentItemRequest> items;
}
