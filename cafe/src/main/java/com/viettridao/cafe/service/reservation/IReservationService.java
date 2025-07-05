package com.viettridao.cafe.service.reservation;

import com.viettridao.cafe.dto.request.reservation.ReservationCreateRequest;
import com.viettridao.cafe.dto.response.reservation.ReservationResponse;

import java.util.List;

public interface IReservationService {

    List<ReservationResponse> getAllReservation();

    void createReservation(ReservationCreateRequest request);

    void deleteReservation(Integer invoiceId);

}
