package com.viettridao.cafe.repository;

import com.viettridao.cafe.dto.account.AccountDTO;
import com.viettridao.cafe.model.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Integer> {
    @Query("select t from TaiKhoan t where t.tenDangNhap = :username")
    Optional<TaiKhoan> findByUsername(String username);

    @Query("""
    SELECT new com.viettridao.cafe.dto.account.AccountDTO(
        nv.hoTen, cv.tenChucVu, nv.diaChi, nv.soDienThoai, cv.luong, tk.tenDangNhap, tk.matKhau, tk.anh)
    FROM TaiKhoan tk
    JOIN tk.nhanVien nv 
    JOIN nv.chucVu cv
    WHERE tk.tenDangNhap = :username
    """)
    AccountDTO findAccountByUsername(@Param("username") String username);
}
