package com.viettridao.cafe.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.model.ReservationKey;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, ReservationKey> {
    boolean existsByTableIdAndReservationDateAndReservationTimeAndIsDeletedFalse(
        Integer tableId, LocalDate date, LocalTime time
    );
    Optional<ReservationEntity> findTopByTable_IdAndIsDeletedFalseOrderByReservationDateDescReservationTimeDesc(Integer tableId);
    
    List<ReservationEntity> findByInvoice_IdAndIsDeletedFalse(Integer invoiceId);

}