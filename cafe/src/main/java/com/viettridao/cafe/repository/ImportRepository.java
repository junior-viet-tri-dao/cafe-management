package com.viettridao.cafe.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.ImportEntity;

/**
 * ImportRepository
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
 * Repository thao tác với thực thể ImportEntity.
 */
@Repository
public interface ImportRepository extends JpaRepository<ImportEntity, Integer> {

    /**
     * Lấy danh sách các phiếu nhập kho chưa bị xóa mềm.
     *
     * @return List<ImportEntity>
     */
    List<ImportEntity> findAllByIsDeletedFalse();

    /**
     * Lấy danh sách các phiếu nhập kho chưa bị xóa mềm theo khoảng ngày nhập.
     *
     * @param start ngày bắt đầu
     * @param end ngày kết thúc
     * @return List<ImportEntity>
     */
    List<ImportEntity> findByImportDateBetweenAndIsDeletedFalse(LocalDate start, LocalDate end);

}