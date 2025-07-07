package com.vn.repository;
import com.vn.model.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.util.List;


@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {
    @Query("SELECT h FROM HoaDon h JOIN ChiTietDatBan ctdb ON h.maHoaDon = ctdb.hoaDon.maHoaDon WHERE ctdb.ban.maBan = :maBan AND h.trangThai = false ORDER BY h.ngayGioTao DESC")
    List<HoaDon> findHoaDonDangMoByMaBan(Integer maBan);
    
    @Query(
    value = """
        SELECT 
            d.Ngay,
            ISNULL((SELECT SUM(tong_tien) FROM dbo.hoa_don hd WHERE hd.trang_thai = 1 AND CAST(hd.ngay_gio_tao AS DATE) = d.Ngay), 0) AS Thu,
            (
                ISNULL((SELECT SUM(tong_tien) FROM dbo.don_nhap dn WHERE dn.ngay_nhap = d.Ngay), 0) +
                ISNULL((SELECT SUM(so_tien) FROM dbo.chi_tieu ct WHERE ct.ngay_chi = d.Ngay), 0) +
                ISNULL((SELECT SUM(so_luong * don_gia_mua) FROM dbo.thiet_bi tb WHERE tb.ngay_nhap = d.Ngay), 0)
            ) AS Chi
        FROM (
            SELECT CAST(ngay_gio_tao AS DATE) AS Ngay FROM dbo.hoa_don WHERE trang_thai = 1
            UNION
            SELECT ngay_chi FROM dbo.chi_tieu
            UNION
            SELECT ngay_nhap FROM dbo.don_nhap
        ) AS d
        WHERE d.Ngay BETWEEN :fromDate AND :toDate
        ORDER BY d.Ngay
    """,
    countQuery = """
        SELECT COUNT(*) FROM (
            SELECT CAST(ngay_gio_tao AS DATE) AS Ngay FROM dbo.hoa_don WHERE trang_thai = 1
            UNION
            SELECT ngay_chi FROM dbo.chi_tieu
            UNION
            SELECT ngay_nhap FROM dbo.don_nhap
        ) AS d
        WHERE d.Ngay BETWEEN :fromDate AND :toDate
    """,
    nativeQuery = true
)
    Page<Object[]> getDoanhThuChiTieuTheoNgay(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, Pageable pageable);



    @Query(
        value =    "SELECT hd.ma_hoa_don, hd.ngay_gio_tao, hd.tong_tien, km.ten_khuyen_mai " +
    "FROM hoa_don hd " +
    "JOIN khuyen_mai km ON hd.ma_khuyen_mai = km.ma_khuyen_mai " +
    "WHERE hd.ngay_gio_tao >= :fromDate " +
    "  AND hd.ngay_gio_tao <  :toDate " +
    "  AND hd.trang_thai =  '1' " +
    "ORDER BY hd.ngay_gio_tao DESC",
        nativeQuery = true)
    Page<Object[]> getHoaDonByDateRangeAndStatus(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate, Pageable pageable);
}


