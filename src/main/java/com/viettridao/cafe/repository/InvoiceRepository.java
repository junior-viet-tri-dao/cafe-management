package com.viettridao.cafe.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.InvoiceEntity;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Integer> {

    InvoiceEntity findByIdAndIsDeletedFalse(Integer id);

    @Query("SELECT i FROM InvoiceEntity i WHERE i.createdAt BETWEEN :start AND :end AND i.isDeleted = false")
    List<InvoiceEntity> findAllByInvoiceDateBetween(@Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

}
