package com.vn.repository;

import com.vn.model.ThietBi;
import com.vn.model.Users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ThietBiRepository extends JpaRepository<ThietBi, Integer> {
      boolean existsByMaThietBi(Integer maThietBi);
      boolean existsByTenThietBi(String tenThietBi);

      @Query("SELECT u FROM ThietBi u WHERE u.isDeleted = false AND (:keyword IS NULL OR u.tenThietBi LIKE %:keyword%)")
    Page<ThietBi> searchEmployee(@Param("keyword") String keyword, Pageable pageable);
}

