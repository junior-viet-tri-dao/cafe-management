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

	private Double change;

	private Integer invoiceId;

	private String invoiceStatus;

	private String paidByName;

	private Integer paidById;

}
