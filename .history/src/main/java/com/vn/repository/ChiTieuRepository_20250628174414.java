package com.vn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.model.ChiTieu;

@Repository
public interface ChiTieuRepository extends JpaRepository<ChiTieu, Integer> {

    boolean existsByMaChiTieu(Integer maChiTieu);

    boolean existsByTenChiTieu(String tenChiTieu);
}
