package com.vn.repository;

import com.vn.model.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.vn.model.Ban;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {
    // Lấy hóa đơn đang mở cho một bàn
    @Query("SELECT h FROM HoaDon h JOIN ChiTietDatBan ctdb ON h.maHoaDon = ctdb.hoaDon.maHoaDon WHERE ctdb.ban.maBan = :maBan AND h.trangThai = false ORDER BY h.ngayGioTao DESC")
    List<HoaDon> findHoaDonDangMoByMaBan(Integer maBan);
} 