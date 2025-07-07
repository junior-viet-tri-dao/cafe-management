package com.vn.repository;

import com.vn.model.Ban;
import com.vn.model.TinhTrangBan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BanRepository extends JpaRepository<Ban, Integer> {
    // @Query("SELECT b FROM Ban b WHERE b.isDeleted = false AND (b.tenBan LIKE %:keyword% OR b.maBan LIKE %:keyword%)")
    // Page<Ban> searchBan(String keyword, Pageable pageable);

    List<Ban> findByTinhTrang(TinhTrangBan tinhTrang);

    @Query("SELECT b FROM Ban b WHERE b.tinhTrang = :tinhTrang")
    List<Ban> findBanByTinhTrang(@Param("tinhTrang") TinhTrangBan tinhTrang);

    @Query("SELECT b.maBan, b.tenBan FROM Ban b WHERE b.tinhTrang = :tinhTrang")
    List<Object[]> findMaBanAndTenBanByTinhTrang(@Param("tinhTrang") TinhTrangBan tinhTrang);
}
