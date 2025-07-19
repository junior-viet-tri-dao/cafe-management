package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.InvoiceDetailEntity;
import com.viettridao.cafe.model.InvoiceKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * InvoiceItemDetailRepository
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
 * Repository thao tác với chi tiết hóa đơn (InvoiceDetailEntity).
 */
public interface InvoiceItemDetailRepository extends JpaRepository<InvoiceDetailEntity, InvoiceKey> {

    /**
     * Lấy danh sách chi tiết hóa đơn chưa bị xóa mềm theo id hóa đơn.
     *
     * @param invoiceId id của hóa đơn
     * @return List<InvoiceDetailEntity>
     */
    List<InvoiceDetailEntity> findByInvoice_IdAndIsDeletedFalse(Integer invoiceId);

    /**
     * Tìm chi tiết hóa đơn theo id và chưa bị xóa mềm.
     *
     * @param id InvoiceKey của chi tiết hóa đơn
     * @return Optional<InvoiceDetailEntity>
     */
    Optional<InvoiceDetailEntity> findByIdAndIsDeletedFalse(InvoiceKey id);

    /**
     * Lấy danh sách chi tiết hóa đơn theo id hóa đơn (không phân biệt trạng thái xóa mềm).
     *
     * @param invoiceId id của hóa đơn
     * @return List<InvoiceDetailEntity>
     */
    List<InvoiceDetailEntity> findByInvoice_Id(Integer invoiceId);
}