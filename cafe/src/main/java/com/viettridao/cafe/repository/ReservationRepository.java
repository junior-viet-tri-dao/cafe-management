package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.model.ReservationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, ReservationKey> {
    Optional<ReservationEntity> findTopByTableIdAndIsDeletedOrderByReservationDateDesc(Integer tableId, boolean isDeleted);
}
