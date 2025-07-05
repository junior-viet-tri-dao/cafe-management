package com.viettridao.cafe.dto.request.tables;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableBookingRequest {

	@NotNull(message = "Id bàn không được để trống")
	private Integer tableId;

	@NotBlank(message = "Tên khách hàng không được để trống")
	@Size(max = 30)
	private String customerName;

	@NotBlank(message = "Số điện thoại không được để trống")
	@Pattern(regexp = "\\d{10,11}", message = "Số điện thoại phải gồm 10 hoặc 11 chữ số")
	private String customerPhone;

	@NotNull(message = "Ngày đặt bàn không được để trống")
	private LocalDate reservationDate;

	@NotNull(message = "Giờ đặt bàn không được để trống")
	private LocalTime reservationTime;

	@NotNull(message = "Nhân viên không được để trống")
	private Integer employeeId;

}
