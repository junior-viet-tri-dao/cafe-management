package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.InvoiceDetailEntity;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.InvoiceKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetailEntity, InvoiceKey> {
    List<InvoiceDetailEntity> findAllByInvoice_Id(Integer invoiceId);

    List<InvoiceDetailEntity> findByInvoice_Id(Integer invoiceId);

}
