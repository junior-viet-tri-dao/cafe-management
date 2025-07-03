package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.model.ReservationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, ReservationKey> {
    @Query("""
    SELECT r FROM ReservationEntity r
    WHERE r.table.id = :tableId
      AND r.isDeleted = false
    ORDER BY r.reservationDate DESC
    """)
    Optional<ReservationEntity> findLatestReservation(@Param("tableId") Integer tableId);

}
