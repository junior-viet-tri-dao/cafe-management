package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.ExportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExportRepository extends JpaRepository<ExportEntity, Integer> {
    List<ExportEntity> findAllByIsDeletedFalseAndExportDateBetween(LocalDate startDate, LocalDate endDate);
}
