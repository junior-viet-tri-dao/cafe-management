package com.viettridao.cafe.service.reservation;

import java.util.List;

import com.viettridao.cafe.dto.request.reservation.ReservationCreateRequest;
import com.viettridao.cafe.dto.response.reservation.ReservationResponse;

public interface IReservationService {

    List<ReservationResponse> getAllReservation();

    void createReservation(ReservationCreateRequest request);

    void deleteReservation(Integer invoiceId);

}
