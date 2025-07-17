package com.viettridao.cafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.InvoiceDetailEntity;
import com.viettridao.cafe.model.InvoiceKey;

@Repository
public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetailEntity, InvoiceKey> {

    List<InvoiceDetailEntity> findByInvoiceId(Integer id);

}
