package com.viettridao.cafe.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.ExportEntity;

/**
 * ExportRepository
 *
 * Version 1.0
 *
 * Date: 19-07-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE         AUTHOR      DESCRIPTION
 * -------------------------------------------------------
 * 19-07-2025   mirodoan    Create
 *
 * Repository thao tác với thực thể ExportEntity.
 */
@Repository
public interface ExportRepository extends JpaRepository<ExportEntity, Integer> {

    /**
     * Lấy danh sách các phiếu xuất kho chưa bị xóa mềm.
     *
     * @return List<ExportEntity>
     */
    List<ExportEntity> findAllByIsDeletedFalse();

    /**
     * Lấy danh sách các phiếu xuất kho chưa bị xóa mềm theo khoảng ngày xuất.
     *
     * @param startDate ngày bắt đầu
     * @param endDate ngày kết thúc
     * @return List<ExportEntity>
     */
    List<ExportEntity> findAllByIsDeletedFalseAndExportDateBetween(LocalDate startDate, LocalDate endDate);
}