package com.viettridao.cafe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.model.ReservationKey;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, ReservationKey> {

    /**
     * Tìm reservation hiện tại (chưa xóa) theo tableId - lấy record mới nhất
     */
    @Query("SELECT r FROM ReservationEntity r WHERE r.table.id = :tableId AND r.isDeleted = false ORDER BY r.reservationDate DESC")
    List<ReservationEntity> findCurrentReservationsByTableId(@Param("tableId") Integer tableId, Pageable pageable);

    /**
     * Helper method để lấy reservation mới nhất
     */
    default Optional<ReservationEntity> findCurrentReservationByTableId(Integer tableId) {
        List<ReservationEntity> reservations = findCurrentReservationsByTableId(tableId,
                org.springframework.data.domain.PageRequest.of(0, 1));
        return reservations.isEmpty() ? Optional.empty() : Optional.of(reservations.get(0));
    }
}
