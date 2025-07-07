package com.vn.repository;

import com.vn.model.ChiTietDatBan;
import com.vn.model.ChiTietDatBan.ChiTietDatBanId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChiTietDatBanRepository extends JpaRepository<ChiTietDatBan, ChiTietDatBanId> {
} 