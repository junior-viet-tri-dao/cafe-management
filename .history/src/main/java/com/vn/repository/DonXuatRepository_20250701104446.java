package com.vn.repository;

import com.vn.model.DonXuat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DonXuatRepository extends JpaRepository<DonXuat, Integer> {

    @Query("SELECT d FROM DonXuat d WHERE (:keyword IS NULL OR d.maDonNhap LIKE %:keyword%)")
    Page<DonXuat> searchDonXuat(@Param("keyword") Integer keyword, Pageable pageable);

} 