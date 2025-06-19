package com.viettridao.cafe.repository;

import com.viettridao.cafe.dto.response.LoginResponse;
import com.viettridao.cafe.dto.response.NhanVienResponse;
import com.viettridao.cafe.model.ChucVu;
import com.viettridao.cafe.model.NhanVien;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {

       @Query(value = """
                     SELECT nv.HoTen,
                            cv.TenChucVu,
                            nv.DiaChi,
                            nv.SoDienThoai,
                            cv.Luong,
                            tk.TenDangNhap,
                            tk.MatKhau
                     FROM dbo.NHANVIEN nv
                     INNER JOIN dbo.CHUCVU cv ON nv.MaChucVu = cv.MaChucVu
                     INNER JOIN dbo.TAIKHOAN tk ON nv.MaTaiKhoan = tk.MaTaiKhoan
                     WHERE tk.TenDangNhap = :username AND nv.isDeleted = 0
                     """, nativeQuery = true)
       LoginResponse findThongTinDangNhap(@Param("username") String username);

       @Query(value = """
                     SELECT nv.MaNhanVien as maNhanVien,
                            nv.HoTen as hoTen,
                            cv.TenChucVu as tenChucVu,
                            cv.Luong as luong
                     FROM dbo.NHANVIEN nv
                     JOIN dbo.CHUCVU cv ON nv.MaChucVu = cv.MaChucVu
                     WHERE nv.isDeleted = 0
                     """, nativeQuery = true)
       List<NhanVienResponse> getListNhanVien();

       @Query("SELECT MAX(n.id) FROM NhanVien n")
       Integer findMaxMaNhanVien();

}
