package com.viettridao.cafe.dto.request.export;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExportRequest {

	@NotNull(message = "ID sản phẩm không được để trống")
	private Integer productId;

	@NotNull(message = "Số lượng không được để trống")
	@Min(value = 1, message = "Số lượng phải lớn hơn 0")
	private Integer quantity;

	@NotNull(message = "Ngày xuất không được để trống")
	@PastOrPresent(message = "Ngày xuất phải là hôm nay hoặc trong quá khứ")
	private LocalDate exportDate;

	@NotNull(message = "ID nhân viên không được để trống")
	private Integer employeeId;
}
