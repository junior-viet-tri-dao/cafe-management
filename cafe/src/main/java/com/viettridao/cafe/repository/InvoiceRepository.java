package com.viettridao.cafe.repository;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.model.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Integer> {
    List<InvoiceEntity> findByStatusAndCreatedAtBetween(InvoiceStatus status, LocalDateTime from, LocalDateTime to);

    InvoiceEntity findTopByReservations_Table_IdAndStatusAndIsDeletedFalseOrderByCreatedAtDesc(Integer tableId,
                                                                                               InvoiceStatus status);

    @Query("SELECT SUM(i.totalAmount) FROM InvoiceEntity i WHERE DATE(i.createdAt) = :date AND i.status = 'PAID' AND i.isDeleted = false")
    Double sumTotalAmountByDate(@Param("date") LocalDate date);

    @Query("SELECT SUM(i.totalAmount) FROM InvoiceEntity i WHERE i.createdAt BETWEEN :from AND :to AND i.status = 'PAID' AND i.isDeleted = false")
    Double sumTotalAmountBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

}
