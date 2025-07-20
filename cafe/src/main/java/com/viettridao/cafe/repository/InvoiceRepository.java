package com.viettridao.cafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.model.InvoiceEntity;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Integer> {

    List<InvoiceEntity> findAllByDeletedFalse();

    List<InvoiceEntity> findByCreatedAtBetweenAndDeletedFalse(LocalDateTime start, LocalDateTime end);

    @Query("SELECT i FROM InvoiceEntity i JOIN ReservationEntity r ON r.invoice.id = i.id WHERE r.table.id = :tableId AND i.deleted = false AND i.status = 'PENDING_PAYMENT'")
    Optional<InvoiceEntity> findInvoiceByTableId(@Param("tableId") Integer tableId);

    List<InvoiceEntity> findAllByReservations_Table_IdAndStatusInAndDeletedFalse(Integer tableId, List<InvoiceStatus> status);


}
