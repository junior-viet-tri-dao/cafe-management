package com.viettridao.cafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.ThucDon;

@Repository
public interface ThucDonRepository extends JpaRepository<ThucDon, Long> {
    List<ThucDon> findByBan_IdAndDaXoaFalse(Long banId);
}

