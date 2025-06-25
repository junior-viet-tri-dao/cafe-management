package com.viettridao.cafe.controller.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Class đại diện cho dữ liệu phản hồi thông tin hồ sơ nhân viên. Được sử dụng
 * để truyền thông tin nhân viên từ hệ thống đến giao diện người dùng.
 */
@Getter
@Setter
public class EmployeeProfileResponse {

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

	// Tên đăng nhập của nhân viên trong hệ thống
	private String username;

	/**
	 * Mật khẩu của nhân viên (lưu ý: nên cân nhắc không bao gồm mật khẩu trong dữ
	 * liệu phản hồi vì lý do bảo mật)
	 */
	private String password;

	// Đường dẫn đến hình ảnh đại diện của nhân viên
	private String imageUrl;
}
