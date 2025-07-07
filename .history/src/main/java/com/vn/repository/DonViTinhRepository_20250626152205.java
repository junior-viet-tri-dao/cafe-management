package com.vn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vn.model.DonViTinh;

@Repository
public interface DonViTinhRepository extends JpaRepository<DonViTinh, Integer> {

    DonViTinh findByName(String name);
    DonViTinh findByMaDonViTinh(Integer maDonViTinh);

}
