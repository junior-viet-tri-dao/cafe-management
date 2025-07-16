package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.InvoiceDetailEntity;
import com.viettridao.cafe.model.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.viettridao.cafe.model.InvoiceKey;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetailEntity, InvoiceKey> {


    List<InvoiceDetailEntity> findAllByInvoice_Id(Integer invoiceId);

    List<InvoiceDetailEntity> findByInvoice_Id(Integer invoiceId);

}
