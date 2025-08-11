package com.viettridao.cafe.repository; // Khai báo gói cho giao diện AccountRepository

import com.viettridao.cafe.model.AccountEntity; // Nhập lớp AccountEntity từ gói model
import com.viettridao.cafe.model.EquipmentEntity; // Nhập lớp EquipmentEntity từ gói model (có thể không được sử dụng trực tiếp trong repository này)
import org.springframework.data.jpa.repository.JpaRepository; // Nhập giao diện JpaRepository từ Spring Data JPA
import org.springframework.data.jpa.repository.Query; // Nhập chú thích Query từ Spring Data JPA (có thể không được sử dụng trực tiếp trong repository này)
import org.springframework.stereotype.Repository; // Nhập chú thích Repository của Spring để đánh dấu giao diện là một repository

import java.util.List; // Nhập lớp List từ gói java.util (có thể không được sử dụng trực tiếp trong repository này)
import java.util.Optional; // Nhập lớp Optional từ gói java.util

/**
 * AccountRepository
 *
 * Giao diện này cung cấp các phương thức truy cập dữ liệu cho AccountEntity.
 * Nó mở rộng JpaRepository để tận dụng các chức năng CRUD được cung cấp bởi Spring Data JPA.
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
@Repository // Đánh dấu giao diện này là một Spring Data JPA Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> { // Kế thừa JpaRepository để có các phương thức CRUD cơ bản cho AccountEntity với khóa chính là Integer

    /**
     * Tìm một AccountEntity theo tên người dùng.
     *
     * @param username Tên người dùng của tài khoản cần tìm.
     * @return Một Optional chứa AccountEntity nếu tìm thấy, ngược lại là Optional trống.
     */
    Optional<AccountEntity> findByUsername(String username); // Khai báo phương thức để tìm tài khoản theo tên người dùng, trả về Optional để xử lý trường hợp không tìm thấy
}
