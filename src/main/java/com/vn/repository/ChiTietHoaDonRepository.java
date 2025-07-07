package com.vn.repository;

import com.vn.model.ChiTietHoaDon;
import com.vn.model.ChiTietHoaDon.ChiTietHoaDonId;
import com.vn.model.HoaDon;
import com.vn.model.ThucDon;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChiTietHoaDonRepository extends JpaRepository<ChiTietHoaDon, ChiTietHoaDonId> {
    ChiTietHoaDon findByHoaDonAndThucDon(HoaDon hoaDon, ThucDon thucDon);
    List<ChiTietHoaDon> findByHoaDon(HoaDon hoaDon);
} 