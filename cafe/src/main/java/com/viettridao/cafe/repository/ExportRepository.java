package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.ExportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ExportRepository extends JpaRepository<ExportEntity, Integer> {
    @Query("SELECT SUM(e.totalExportAmount) FROM ExportEntity e WHERE e.exportDate = :date AND e.isDeleted = false")
    Double sumTotalExportAmountByDate(@Param("date") LocalDate date);


}
