package com.viettridao.cafe.dto.request.Pay;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentItemRequest {

	@NotNull(message = "ID món không được để trống")
	private Integer menuItemId;

	@NotNull(message = "Số lượng không được để trống")
	@Min(value = 1, message = "Số lượng phải lớn hơn 0")
	private Integer quantity;

	@NotNull(message = "Giá không được để trống")
	@Positive(message = "Giá phải lớn hơn 0")
	private Double price;

	@NotNull(message = "Thành tiền không được để trống")
	@PositiveOrZero(message = "Thành tiền phải lớn hơn hoặc bằng 0")
	private Double amount;
}
