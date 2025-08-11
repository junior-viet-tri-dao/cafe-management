package com.viettridao.cafe.service; // Khai báo gói cho giao diện AccountService

import com.viettridao.cafe.dto.request.account.UpdateAccountRequest; // Nhập lớp UpdateAccountRequest từ gói DTO
import com.viettridao.cafe.model.AccountEntity; // Nhập lớp AccountEntity từ gói model

/**
 * AccountService
 *
 * Giao diện này định nghĩa các phương thức liên quan đến quản lý tài khoản người dùng.
 *
 * Phiên bản 1.0
 *
 * Ngày: 2025-07-23
 *
 * Bản quyền (c) 2025 VietTriDao. Đã đăng ký bản quyền.
 *
 * Nhật ký sửa đổi:
 * NGÀY                 TÁC GIẢ          MÔ TẢ
 * -----------------------------------------------------------------------
 * 2025-07-23           Hoa Mãn Lâu     Tạo và định dạng ban đầu.
 */
public interface AccountService {

    /**
     * Lấy thông tin tài khoản theo ID.
     *
     * @param id ID của tài khoản cần lấy.
     * @return Đối tượng AccountEntity tương ứng với ID đã cho.
     */
    AccountEntity getAccountById(Integer id); // Khai báo phương thức để lấy tài khoản theo ID

    /**
     * Cập nhật thông tin tài khoản.
     *
     * @param request Đối tượng UpdateAccountRequest chứa thông tin cập nhật cho tài khoản.
     */
    void updateAccount(UpdateAccountRequest request); // Khai báo phương thức để cập nhật tài khoản

    /**
     * Lấy thông tin tài khoản theo tên người dùng.
     *
     * @param name Tên người dùng của tài khoản cần lấy.
     * @return Đối tượng AccountEntity tương ứng với tên người dùng đã cho.
     */
    AccountEntity getAccountByUsername(String name); // Khai báo phương thức để lấy tài khoản theo tên người dùng
}
