package com.viettridao.cafe.dto.response.Pay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentResponse {
	private boolean success;
	private String message;
	private Double totalAmount;
	private Double customerCash;
	private Double change; // tiền thối lại
	private Integer invoiceId;
	private String invoiceStatus; // ✅ thêm thông tin trạng thái hóa đơn
	private String paidByName;  // 👈 tên nhân viên
    private Integer paidById;  
}

