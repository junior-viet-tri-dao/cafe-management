package com.viettridao.cafe.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.response.reservation.ReservationResponse;
import com.viettridao.cafe.model.ReservationEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ReservationMapper {

    private final ModelMapper modelMapper;

    public ReservationResponse convertToResponse(ReservationEntity reservationEntity) {
        return modelMapper.map(reservationEntity, ReservationResponse.class);
    }
}
