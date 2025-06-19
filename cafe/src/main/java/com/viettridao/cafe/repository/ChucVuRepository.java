package com.viettridao.cafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.ChucVu;

@Repository
public interface ChucVuRepository extends JpaRepository<ChucVu, Integer> {

}
