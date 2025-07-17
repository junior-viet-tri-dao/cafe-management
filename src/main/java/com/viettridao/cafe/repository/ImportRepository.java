package com.viettridao.cafe.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.ImportEntity;

@Repository
public interface ImportRepository extends JpaRepository<ImportEntity, Integer> {

    @Query("SELECT i FROM ImportEntity i WHERE i.importDate BETWEEN :startDate AND :endDate AND i.isDeleted = false")
    List<ImportEntity> findAllByImportDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
