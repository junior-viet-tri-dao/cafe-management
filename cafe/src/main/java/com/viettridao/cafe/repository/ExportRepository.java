
package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.ExportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExportRepository extends JpaRepository<ExportEntity, Integer> {
    // Thêm method hỗ trợ report xuất theo ngày
    java.util.List<ExportEntity> findAllByExportDateBetween(java.time.LocalDate from, java.time.LocalDate to);
}
