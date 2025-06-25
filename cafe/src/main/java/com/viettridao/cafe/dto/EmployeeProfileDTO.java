package com.viettridao.cafe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lớp DTO (Data Transfer Object) dùng để truyền tải thông tin hồ sơ nhân viên.
 * Chứa các thuộc tính liên quan đến thông tin cá nhân và tài khoản của nhân
 * viên. Được sử dụng để gửi dữ liệu giữa các lớp (ví dụ: từ Service đến
 * Controller hoặc ngược lại).
 */
@Getter
@Setter
@AllArgsConstructor // Tự động tạo constructor với tất cả các tham số
@NoArgsConstructor // Tự động tạo constructor không có tham số
public class EmployeeProfileDTO {
	// Mã định danh duy nhất của nhân viên
	private Integer id;
	// Họ và tên đầy đủ của nhân viên
	private String fullName;
	// Chức vụ của nhân viên (ví dụ: Quản lý, Nhân viên phục vụ)
	private String position;
	// Địa chỉ hiện tại của nhân viên
	private String address;
	// Số điện thoại liên hệ của nhân viên
	private String phoneNumber;
	// Mức lương của nhân viên
	private Double salary;
	// Tên đăng nhập của nhân viên
	private String username;
	// Mật khẩu của nhân viên (cần cẩn trọng khi truyền đi, có thể không nên bao gồm
	// trong DTO phản hồi)
	private String password;
	// Đường dẫn đến hình ảnh đại diện của nhân viên
	private String imageUrl;
}