package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.reservation.ReservationRequest;
import com.viettridao.cafe.dto.response.reservation.ReservationResponse;

public interface ReservationService {
    ReservationResponse createReservation(ReservationRequest request, Integer employeeId);

    void cancelReservation(Integer invoiceId);

}
