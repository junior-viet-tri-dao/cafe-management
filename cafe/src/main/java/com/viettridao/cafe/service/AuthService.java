package com.viettridao.cafe.service;

/**
 * Service cho chức năng xác thực (Authentication).
 * Chịu trách nhiệm xử lý logic nghiệp vụ liên quan đến đăng nhập và xác thực
 * người dùng.
 */
public interface AuthService {

    /**
     * Xác thực thông tin đăng nhập của người dùng.
     *
     * @param username Tên đăng nhập của người dùng.
     * @param password Mật khẩu của người dùng.
     * @return true nếu thông tin đăng nhập hợp lệ, ngược lại false.
     */
    boolean login(String username, String password);
}
