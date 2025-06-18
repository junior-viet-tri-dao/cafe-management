package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Integer> {

}
