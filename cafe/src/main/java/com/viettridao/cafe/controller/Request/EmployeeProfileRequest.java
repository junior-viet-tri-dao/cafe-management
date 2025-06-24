package com.viettridao.cafe.controller.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/**
 * Lớp DTO (Data Transfer Object) dùng để nhận dữ liệu yêu cầu cập nhật hồ sơ
 * nhân viên từ client. Chứa các thuộc tính liên quan đến thông tin cá nhân và
 * tài khoản của nhân viên với các ràng buộc validate.
 */

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

	@NotNull(message = "Lương không được để trống")
	@Min(value = 1000, message = "Lương phải lớn hơn 0")
	private Double salary;

	private String imageUrl;
}