package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.UnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * UnitRepository
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
 * Repository cho thực thể UnitEntity.
 * Chịu trách nhiệm truy vấn dữ liệu liên quan đến đơn vị (Unit) từ cơ sở dữ liệu.
 */
@Repository
public interface UnitRepository extends JpaRepository<UnitEntity, Integer> {

    /**
     * Lấy danh sách tất cả các đơn vị dựa trên trạng thái xóa.
     *
     * @param isDeleted Trạng thái xóa của đơn vị (true: đã xóa, false: chưa xóa).
     * @return Danh sách các thực thể UnitEntity theo trạng thái xóa.
     */
    List<UnitEntity> findAllByIsDeleted(Boolean isDeleted);
}