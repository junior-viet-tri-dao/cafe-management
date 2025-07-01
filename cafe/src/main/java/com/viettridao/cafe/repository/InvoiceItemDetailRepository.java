package com.viettridao.cafe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.InvoiceDetailEntity;
import com.viettridao.cafe.model.InvoiceKey;

@Repository
public interface InvoiceItemDetailRepository extends JpaRepository<InvoiceDetailEntity, InvoiceKey> {

	// Lấy tất cả món trong 1 hóa đơn (chưa xóa)
	List<InvoiceDetailEntity> findByInvoice_IdAndIsDeletedFalse(Integer invoiceId);

	// Lấy 1 món trong hóa đơn nếu chưa xóa
	Optional<InvoiceDetailEntity> findByIdAndIsDeletedFalse(InvoiceKey id);

	// Lấy tất cả chi tiết của 1 hóa đơn (bao gồm cả đã xóa)
	List<InvoiceDetailEntity> findByInvoice_Id(Integer invoiceId);
}
