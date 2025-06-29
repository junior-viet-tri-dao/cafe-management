package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.ExportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExportRepository extends JpaRepository<ExportEntity, Integer> {
    // Tìm các phiếu xuất theo tên sản phẩm (nếu có keyword)
    org.springframework.data.domain.Page<ExportEntity> findByProduct_ProductNameContainingIgnoreCaseAndIsDeletedFalse(
            String keyword, org.springframework.data.domain.Pageable pageable);

    // Tìm tất cả phiếu xuất nếu không có keyword
    org.springframework.data.domain.Page<ExportEntity> findByIsDeletedFalse(
            org.springframework.data.domain.Pageable pageable);

    // Hỗ trợ report xuất theo ngày (cho các test/report cũ)
    java.util.List<ExportEntity> findAllByExportDateBetween(java.time.LocalDate from, java.time.LocalDate to);
}
