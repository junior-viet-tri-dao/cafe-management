package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.model.ReservationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, ReservationKey> {

    List<ReservationEntity> findAllByDeletedFalse();

    boolean existsById_IdTableAndReservationDateAndDeletedFalse(Integer tableId, LocalDateTime reservationDateTime);

    Optional<ReservationEntity> findByInvoice_Id(Integer invoiceId);

}
