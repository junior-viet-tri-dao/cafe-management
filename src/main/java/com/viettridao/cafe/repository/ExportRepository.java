package com.viettridao.cafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

import com.viettridao.cafe.model.ExportEntity;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExportRepository extends JpaRepository<ExportEntity, Integer> {

    @Query("SELECT e FROM ExportEntity e WHERE e.exportDate BETWEEN :start AND :end AND e.isDeleted = false")
    List<ExportEntity> findAllByExportDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

}
