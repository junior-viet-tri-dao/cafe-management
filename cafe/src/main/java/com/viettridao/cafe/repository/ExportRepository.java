package com.viettridao.cafe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.ExportEntity;

@Repository
public interface ExportRepository extends JpaRepository<ExportEntity, Integer> {
    Page<ExportEntity> findByProduct_ProductNameContainingIgnoreCase(String keyword, Pageable pageable);

}
