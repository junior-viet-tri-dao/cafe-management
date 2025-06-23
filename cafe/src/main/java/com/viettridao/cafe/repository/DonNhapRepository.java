package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.DonNhap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonNhapRepository extends JpaRepository<DonNhap, Integer> {
}
