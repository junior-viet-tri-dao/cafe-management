package com.vn.repository;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vn.model.DonNhap;
import com.vn.model.DonXuat;

@Repository
public interface DonNhapRepository extends JpaRepository<DonNhap, Integer> {
}
