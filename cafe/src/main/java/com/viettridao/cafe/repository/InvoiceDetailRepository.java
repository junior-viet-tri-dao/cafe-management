package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.InvoiceDetailEntity;
import com.viettridao.cafe.model.InvoiceKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetailEntity, InvoiceKey> {

    @Query("SELECT d FROM InvoiceDetailEntity d " +
            "JOIN d.invoice i " +
            "WHERE i.id = :invoiceId")
    List<InvoiceDetailEntity>findByInvoiceId(@Param("invoiceId") Integer id);
}
