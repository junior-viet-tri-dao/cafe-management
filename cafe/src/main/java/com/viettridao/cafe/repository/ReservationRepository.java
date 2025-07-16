package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.model.ReservationKey;
import com.viettridao.cafe.model.UnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, ReservationKey> {

//    @Query("Select p from ReservationEntity p where p.isDeleted = false and table.id = :tableId LIMIT 1")
    Optional<ReservationEntity> findTopByTableIdAndIsDeletedOrderByReservationDateDesc(Integer tableId, boolean isDeleted);

}
