package com.viettridao.cafe.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.viettridao.cafe.dto.request.tables.TableBookingRequest;
import com.viettridao.cafe.dto.response.tables.TableBookingResponse;
import com.viettridao.cafe.model.ReservationEntity;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

	@Mapping(target = "id", ignore = true) // nếu ID tự tăng
	@Mapping(target = "isDeleted", constant = "false")
	ReservationEntity fromRequest(TableBookingRequest request);

	TableBookingResponse toDto(ReservationEntity entity);
}
