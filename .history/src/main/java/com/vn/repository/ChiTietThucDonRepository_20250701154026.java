package com.vn.repository;

import com.vn.model.ChiTietThucDon;
import com.vn.model.ChiTietThucDon.ChiTietThucDonId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChiTietThucDonRepository extends JpaRepository<ChiTietThucDon, ChiTietThucDonId> {
} 