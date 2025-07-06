package com.viettridao.cafe.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.ExportEntity;

@Repository
public interface ExportRepository extends JpaRepository<ExportEntity, Integer> {

    List<ExportEntity> findAllByDeletedFalse();

    List<ExportEntity> findAllByDeletedFalseAndExportDateBetween(LocalDate startDate, LocalDate endDate);
}
