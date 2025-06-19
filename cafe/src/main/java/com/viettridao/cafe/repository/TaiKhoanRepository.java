package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Integer> {

    @Query("SELECT MAX(t.id) FROM TaiKhoan t")
    Integer findMaxMaNhanVien();

    // Thêm method kiểm tra tên đăng nhập đã tồn tại
    boolean existsByTenDangNhap(String tenDangNhap);
}
