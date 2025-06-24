package com.viettridao.cafe.controller.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeProfileRequest {
	private Integer id;

	@NotBlank(message = "Họ và tên không được để trống")
	private String fullName;

	@NotBlank(message = "Chức vụ không được để trống")
	private String position;

	@NotBlank(message = "Địa chỉ không được để trống")
	private String address;

	@NotBlank(message = "Số điện thoại không được để trống")
	@Pattern(regexp = "\\d{10,11}", message = "Số điện thoại không hợp lệ")
	private String phoneNumber;

	private Double salary;
	private String imageUrl;
}