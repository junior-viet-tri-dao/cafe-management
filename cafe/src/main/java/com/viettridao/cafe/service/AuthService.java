package com.viettridao.cafe.service;

/**
 * Giao diện định nghĩa dịch vụ xác thực người dùng. Cung cấp phương thức để xử
 * lý logic đăng nhập.
 */
public interface AuthService {
	/**
	 * Xử lý đăng nhập người dùng dựa trên tên đăng nhập và mật khẩu được cung cấp.
	 *
	 * @param username Tên đăng nhập mà người dùng nhập vào.
	 * @param password Mật khẩu mà người dùng nhập vào.
	 * @return **true** nếu quá trình đăng nhập thành công (tên đăng nhập và mật
	 *         khẩu hợp lệ), **false** nếu đăng nhập thất bại (sai tên đăng nhập
	 *         hoặc mật khẩu, hoặc lỗi khác).
	 */
	boolean login(String username, String password);
}