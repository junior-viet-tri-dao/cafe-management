package com.vn.repository;

import com.vn.model.ThucDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThucDonRepository extends JpaRepository<ThucDon, Integer> {
    // Define custom query methods if needed
    boolean existsByTenMon(String tenMon);
}
