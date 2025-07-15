
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

    List<InvoiceDetailEntity> findAllByInvoice_IdAndIsDeletedFalse(Integer invoiceId);

}
