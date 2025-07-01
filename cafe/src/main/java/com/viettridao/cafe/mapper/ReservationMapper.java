package com.viettridao.cafe.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.request.tables.TableBookingRequest;
import com.viettridao.cafe.dto.response.tables.TableBookingResponse;
import com.viettridao.cafe.mapper.base.BaseMapper;
import com.viettridao.cafe.model.ReservationEntity;

@Component
public class ReservationMapper extends BaseMapper<ReservationEntity, TableBookingRequest, TableBookingResponse> {

    public ReservationMapper(ModelMapper modelMapper) {
        super(modelMapper, ReservationEntity.class, TableBookingRequest.class, TableBookingResponse.class);
    }

    // ❌ KHÔNG dùng @Override vì BaseMapper không khai báo hàm này
    public ReservationEntity toEntity(TableBookingRequest request) {
        ReservationEntity entity = new ReservationEntity();

        entity.setCustomerName(request.getCustomerName());
        entity.setCustomerPhone(request.getCustomerPhone());
        entity.setReservationDate(request.getReservationDate());
        entity.setReservationTime(request.getReservationTime());
        entity.setIsDeleted(false);

        return entity;
    }
}
