package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.ExportEntity;
import com.viettridao.cafe.model.ImportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExportRepository extends JpaRepository<ExportEntity, Integer> {
    @Query("SELECT SUM(e.totalExportAmount) FROM ExportEntity e WHERE e.exportDate = :date AND e.isDeleted = false")
    Double sumTotalExportAmountByDate(@Param("date") LocalDate date);

    @Query("Select p from ExportEntity p where p.isDeleted = false and (p.totalExportAmount) = :keyword")
    Page<ExportEntity> getAllExportPageSearch(@Param("keyword") String keyword, Pageable pageable);


    @Query("Select p from ExportEntity p where p.isDeleted = false ")
    Page<ExportEntity> getAllExportPage(Pageable pageable);

    @Query("Select p from ExportEntity p where p.isDeleted = false and product.id = :productId")
    List<ExportEntity> getAllProductExportByIdProduct(Integer productId);
}
