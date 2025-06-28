package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.ExportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExportRepository extends JpaRepository<ExportEntity, Integer> {
}
