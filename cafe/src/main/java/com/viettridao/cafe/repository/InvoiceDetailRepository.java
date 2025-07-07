
package com.viettridao.cafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.InvoiceDetailEntity;
import com.viettridao.cafe.model.InvoiceKey;

import java.util.List;

/**
 * Repository thao tác với chi tiết hóa đơn (InvoiceDetailEntity)
 */
@Repository
public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetailEntity, InvoiceKey> {
    /**
     * Lấy danh sách chi tiết hóa đơn theo id hóa đơn (chỉ lấy các dòng chưa bị xóa
     * mềm)
     * 
     * @param invoiceId id hóa đơn
     * @return danh sách chi tiết hóa đơn
     */
    List<InvoiceDetailEntity> findAllByInvoice_IdAndIsDeletedFalse(Integer invoiceId);
}
