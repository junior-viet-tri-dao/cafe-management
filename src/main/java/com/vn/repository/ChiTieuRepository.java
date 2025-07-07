package com.vn.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vn.model.ChiTieu;


@Repository
public interface ChiTieuRepository extends JpaRepository<ChiTieu, Integer> {

    boolean existsByMaChiTieu(Integer maChiTieu);

    boolean existsByTenKhoanChi(String tenKhoanChi);

    @Query("SELECT u FROM ChiTieu u WHERE (:keyword IS NULL OR u.tenKhoanChi LIKE %:keyword%)")
    Page<ChiTieu> searchChiTieu(@Param("keyword") String keyword, Pageable pageable);
}
