package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.reservation.ReservationCreateRequest;
import com.viettridao.cafe.model.ReservationEntity;

public interface ReservationService {
    ReservationEntity createReservation(ReservationCreateRequest request);
}
