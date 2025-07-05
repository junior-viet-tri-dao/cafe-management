package com.viettridao.cafe.dto.request.menu;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuDetailRequest {

	@NotBlank(message = "Tên sản phẩm không được để trống")
	private String productName;

	@NotNull(message = "Số lượng không được để trống")
	@Positive(message = "Số lượng phải lớn hơn 0")
	private Double quantity;

	@NotBlank(message = "Đơn vị tính không được để trống")
	private String unitName;
}
