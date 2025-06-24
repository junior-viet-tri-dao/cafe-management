package com.viettridao.cafe.service;

/**
 * Interface định nghĩa dịch vụ xác thực người dùng. Cung cấp phương thức để xử
 * lý đăng nhập.
 */
public interface AuthService {
	/**
	 * Xử lý đăng nhập người dùng dựa trên tên đăng nhập và mật khẩu.
	 * 
	 * @param username Tên đăng nhập của người dùng
	 * @param password Mật khẩu của người dùng
	 * @return true nếu đăng nhập thành công, false nếu thất bại
	 */
	boolean login(String username, String password);
}