package com.viettridao.cafe.service;

import com.viettridao.cafe.model.ReservationEntity;
import java.util.List;

public interface ReservationService {
    List<ReservationEntity> getAllReservations();

    List<ReservationEntity> getReservationsByTable(Integer tableId);

    ReservationEntity createReservation(ReservationEntity reservation);

    void deleteReservation(ReservationEntity reservation);
}
