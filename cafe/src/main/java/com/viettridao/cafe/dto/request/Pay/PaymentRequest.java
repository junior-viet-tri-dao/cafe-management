package com.viettridao.cafe.dto.request.Pay;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest {

	@NotNull(message = "ID bàn không được để trống")
	private Integer tableId;

	@NotNull(message = "Tiền khách đưa không được để trống")
	@PositiveOrZero(message = "Tiền khách đưa phải lớn hơn hoặc bằng 0")
	private Double customerCash;

	@NotNull(message = "Trạng thái bàn không được để trống")
	private Boolean freeTable;

	@NotNull(message = "Danh sách món thanh toán không được để trống")
	@Valid
	private List<PaymentItemRequest> items;
}
