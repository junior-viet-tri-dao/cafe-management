package com.vn.repository;

import com.vn.model.ThucDon;
import com.vn.model.Users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ThucDonRepository extends JpaRepository<ThucDon, Integer> {
    boolean existsByTenMon(String tenMon);

    @Query("SELECT u FROM ThucDon u WHERE (:keyword IS NULL OR u.tenMon LIKE %:keyword%)")
    Page<ThucDon> searchThucDon(@Param("keyword") String keyword, Pageable pageable);
}
