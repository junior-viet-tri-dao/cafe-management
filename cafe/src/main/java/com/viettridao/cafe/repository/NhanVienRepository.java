package com.viettridao.cafe.repository;

import com.viettridao.cafe.dto.employee.EmployeeDTO;
import com.viettridao.cafe.dto.employee.UpdateEmployeeDTO;
import com.viettridao.cafe.model.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {
    @Query("""
    select nv.maNhanVien, nv.hoTen, cv.tenChucVu, cv.luong from NhanVien nv join nv.chucVu cv
    where nv.isDeleted = false 
    """)
    List<EmployeeDTO> getAllByEmployee();

    @Query("""
    SELECT new com.viettridao.cafe.dto.employee.UpdateEmployeeDTO(
         nv.maNhanVien, nv.hoTen, cv.maChucVu, nv.diaChi, nv.soDienThoai,
         cv.luong, tk.tenDangNhap, tk.matKhau, tk.anh)
    FROM NhanVien nv
    JOIN nv.chucVu cv
    LEFT JOIN nv.taiKhoan tk
    WHERE nv.maNhanVien = :id
    """)
    UpdateEmployeeDTO getEmployeeById(@Param("id") Integer id);
}
