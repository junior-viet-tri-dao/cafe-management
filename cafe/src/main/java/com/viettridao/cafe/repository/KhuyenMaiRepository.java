package com.viettridao.cafe.repository;

import com.viettridao.cafe.dto.marketing.MarketingDTO;
import com.viettridao.cafe.model.KhuyenMai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KhuyenMaiRepository extends JpaRepository<KhuyenMai, Integer> {
    @Query("""
    SELECT new com.viettridao.cafe.dto.marketing.MarketingDTO(
        t.maKhuyenMai, t.tenKhuyenMai, t.ngayBatDau, t.ngayKetThuc, t.giaTriGiam) 
        FROM KhuyenMai t where t.isDeleted = false 
    """)
    List<MarketingDTO> getAllMarketings();

    @Query("""
    SELECT new com.viettridao.cafe.dto.marketing.MarketingDTO(
        t.maKhuyenMai, t.tenKhuyenMai, t.ngayBatDau, t.ngayKetThuc, t.giaTriGiam) 
        FROM KhuyenMai t where t.maKhuyenMai = :id
    """)
    MarketingDTO getMarketingById(Integer id);
}
