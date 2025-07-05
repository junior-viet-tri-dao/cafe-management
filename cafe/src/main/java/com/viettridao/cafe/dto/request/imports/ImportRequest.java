package com.viettridao.cafe.dto.request.imports;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportRequest {

	@NotNull(message = "ID sản phẩm không được để trống")
	private Integer productId;

	@NotNull(message = "Số lượng không được để trống")
	@Min(value = 1, message = "Số lượng phải lớn hơn 0")
	private Integer quantity;

	@NotNull(message = "Ngày nhập không được để trống")
	@PastOrPresent(message = "Ngày nhập phải là hôm nay hoặc trong quá khứ")
	private LocalDate importDate;

	@NotNull(message = "ID nhân viên không được để trống")
	private Integer employeeId;

	@NotNull(message = "Giá sản phẩm không được để trống")
	@Positive(message = "Giá sản phẩm phải lớn hơn 0")
	private Double productPrice;

	@NotBlank(message = "Đơn vị tính không được để trống")
	private String unitName;
}
