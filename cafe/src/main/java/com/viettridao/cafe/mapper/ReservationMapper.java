package com.viettridao.cafe.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.viettridao.cafe.dto.request.reservation.ReservationCreateRequest;
import com.viettridao.cafe.model.*;
import com.viettridao.cafe.dto.response.reservation.ReservationResponse;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "deleted", constant = "false")
    ReservationEntity toEntity(ReservationCreateRequest request);


    @Mapping(target = "tableName", source = "table.tableName")
    @Mapping(target = "employeeName", source = "employee.fullName")
    ReservationResponse toResponse(ReservationEntity entity);

}
