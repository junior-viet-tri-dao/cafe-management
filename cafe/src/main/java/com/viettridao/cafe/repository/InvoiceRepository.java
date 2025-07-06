package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.PositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Integer> {
    @Query("SELECT SUM(i.totalAmount) FROM InvoiceEntity i WHERE DATE(i.createdAt) = :date AND i.status = 'PAID' AND i.isDeleted = false")
    Double sumTotalAmountByDate(@Param("date") LocalDate date);


}
