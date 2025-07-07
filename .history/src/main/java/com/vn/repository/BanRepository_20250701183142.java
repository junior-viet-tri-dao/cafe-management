package com.vn.repository;

import com.vn.model.Ban;
import com.vn.model.TinhTrangBan;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BanRepository extends JpaRepository<Ban, Integer> {
    // @Query("SELECT b FROM Ban b WHERE b.isDeleted = false AND (b.tenBan LIKE %:keyword% OR b.maBan LIKE %:keyword%)")
    // Page<Ban> searchBan(String keyword, Pageable pageable);

    List<Ban> findByTinhTrang(TinhTrangBan tinhTrang);

    
}
