package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.reservation.CreateReservationRequest;
import com.viettridao.cafe.dto.request.table.CreateTableRequest;
import com.viettridao.cafe.model.ReservationEntity;
import jakarta.validation.Valid;

import java.util.List;

public interface ReservationService {

    ReservationEntity getReservationById(Integer id);

    ReservationEntity createReservation(CreateReservationRequest request);
}
