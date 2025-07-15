package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.InvoiceDetailEntity;
import com.viettridao.cafe.model.InvoiceKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvoiceItemDetailRepository extends JpaRepository<InvoiceDetailEntity, InvoiceKey> {
    List<InvoiceDetailEntity> findByInvoice_IdAndIsDeletedFalse(Integer invoiceId);

    Optional<InvoiceDetailEntity> findByIdAndIsDeletedFalse(InvoiceKey id);

    List<InvoiceDetailEntity> findByInvoice_Id(Integer invoiceId);

//    void save(InvoiceDetailEntity detail);
}
