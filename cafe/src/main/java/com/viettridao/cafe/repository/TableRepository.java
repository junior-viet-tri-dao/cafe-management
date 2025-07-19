package com.viettridao.cafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.model.TableEntity;

/**
 * TableRepository
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
 * Repository thao tác với thực thể TableEntity.
 */
@Repository
public interface TableRepository extends JpaRepository<TableEntity, Integer> {

    /**
     * Lấy danh sách bàn theo trạng thái.
     *
     * @param status trạng thái bàn
     * @return List<TableEntity>
     */
    List<TableEntity> findByStatus(TableStatus status);

    /**
     * Tìm bàn theo id hóa đơn của reservation.
     *
     * @param invoiceId id hóa đơn
     * @return TableEntity
     */
    TableEntity findByReservations_Invoice_Id(Integer invoiceId);
}