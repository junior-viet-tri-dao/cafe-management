package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.ThanhPhanThucDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThanhPhanThucDonRepository extends JpaRepository<ThanhPhanThucDon, Integer> {
}
