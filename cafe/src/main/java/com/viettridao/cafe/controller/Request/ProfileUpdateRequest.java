package com.viettridao.cafe.controller.Request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Class đại diện cho yêu cầu chỉnh sửa thông tin cơ bản của hồ sơ nhân viên.
 * Không cho phép thay đổi tên đăng nhập và mật khẩu.
 */
@Getter
@Setter
public class ProfileUpdateRequest {

	// Mã định danh duy nhất của nhân viên
	private Integer id;

	// Họ và tên đầy đủ, không được để trống, tối đa 30 ký tự
	@NotBlank(message = "Họ và tên không được để trống")
	@Size(max = 30, message = "Họ và tên không được vượt quá 30 ký tự")
	private String fullName;

	// Chức vụ của nhân viên, không được để trống, tối đa 20 ký tự
	@NotBlank(message = "Chức vụ không được để trống")
	@Size(max = 20, message = "Chức vụ không được vượt quá 20 ký tự")
	private String position;

	// Địa chỉ hiện tại, không được để trống, tối đa 50 ký tự
	@NotBlank(message = "Địa chỉ không được để trống")
	@Size(max = 50, message = "Địa chỉ không được vượt quá 50 ký tự")
	private String address;

	// Số điện thoại, phải là chuỗi số từ 10 đến 15 ký tự, không được để trống
	@NotBlank(message = "Số điện thoại không được để trống")
	@Pattern(regexp = "\\d{10,15}", message = "Số điện thoại không hợp lệ (phải từ 10 đến 15 chữ số)")
	private String phoneNumber;

	// Mức lương, không được để trống, phải từ 1.000.000 VNĐ trở lên
	@NotNull(message = "Lương không được để trống")
	@DecimalMin(value = "1000000", message = "Lương phải từ 1.000.000 VNĐ trở lên")
	private Double salary;

	// Đường dẫn ảnh đại diện, tối đa 255 ký tự, có thể null
	@Size(max = 255, message = "Link ảnh không vượt quá 255 ký tự")
	private String imageUrl;
}
