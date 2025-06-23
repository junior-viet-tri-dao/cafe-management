package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.DonXuat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonXuatRepository extends JpaRepository<DonXuat, Integer> {
}
