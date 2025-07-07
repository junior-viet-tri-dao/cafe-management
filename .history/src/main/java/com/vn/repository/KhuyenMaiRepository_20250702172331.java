package com.vn.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vn.model.KhuyenMai;
import com.vn.model.ThietBi;

@Repository   
public interface KhuyenMaiRepository extends JpaRepository<KhuyenMai, Integer> {

    boolean existsByTenKhuyenMai(String tenKhuyenMai);

    @Query("SELECT u FROM KhuyenMai u WHERE u.isDeleted = false AND (:keyword IS NULL OR u.tenKhuyenMai LIKE %:keyword%)")
    Page<KhuyenMai> searchKhuyenMai(@Param("keyword") String keyword, Pageable pageable);
}


    

