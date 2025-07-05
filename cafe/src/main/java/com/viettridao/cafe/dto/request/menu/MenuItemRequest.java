package com.viettridao.cafe.dto.request.menu;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuItemRequest {

	@NotBlank(message = "Tên món không được để trống")
	private String itemName;

	@NotNull(message = "Giá hiện tại không được để trống")
	@Positive(message = "Giá hiện tại phải lớn hơn 0")
	private Double currentPrice;

	@NotNull(message = "Danh sách nguyên liệu không được để trống")
	@Valid
	private List<MenuDetailRequest> ingredients;
}
