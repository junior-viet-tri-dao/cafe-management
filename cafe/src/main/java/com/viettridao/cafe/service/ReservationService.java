package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.reservation.ReservationCreateRequest;
import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.model.ReservationKey;

public interface ReservationService {
    ReservationEntity createReservation(ReservationCreateRequest request);
    ReservationEntity getReservationById(Integer tableId);
}
