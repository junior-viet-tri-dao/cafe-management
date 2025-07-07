package com.vn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.model.KhuyenMai;

@Repository   
public interface KhuyenMaiRepository extends JpaRepository<KhuyenMai, Integer> {

    boolean existsByTenKhuyenMai(String tenKhuyenMai);

    // Additional query methods can be defined here if needed
    
}
