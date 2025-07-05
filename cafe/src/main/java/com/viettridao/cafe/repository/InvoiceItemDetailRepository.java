package com.viettridao.cafe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.InvoiceDetailEntity;
import com.viettridao.cafe.model.InvoiceKey;

@Repository
public interface InvoiceItemDetailRepository extends JpaRepository<InvoiceDetailEntity, InvoiceKey> {

	List<InvoiceDetailEntity> findByInvoice_IdAndIsDeletedFalse(Integer invoiceId);

	Optional<InvoiceDetailEntity> findByIdAndIsDeletedFalse(InvoiceKey id);

	List<InvoiceDetailEntity> findByInvoice_Id(Integer invoiceId);
}
