package com.viettridao.cafe.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.model.InvoiceEntity;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Integer> {

	List<InvoiceEntity> findByStatusAndCreatedAtBetween(InvoiceStatus status, LocalDateTime from, LocalDateTime to);

	InvoiceEntity findTopByReservations_Table_IdAndStatusAndIsDeletedFalseOrderByCreatedAtDesc(Integer tableId,
			InvoiceStatus status);

}
