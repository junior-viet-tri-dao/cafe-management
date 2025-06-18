package com.viettridao.cafe.repository;

import com.viettridao.cafe.dto.response.ThongTinDangNhapResponse;
import com.viettridao.cafe.model.NhanVien;
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
            FROM NHANVIEN nv
            INNER JOIN CHUCVU cv ON nv.MaChucVu = cv.MaChucVu
            INNER JOIN TAIKHOAN tk ON nv.MaTaiKhoan = tk.MaTaiKhoan
            WHERE tk.TenDangNhap = :username AND nv.isDeleted = 0
            """, nativeQuery = true)
    ThongTinDangNhapResponse findThongTinDangNhap(@Param("username") String username);


}
