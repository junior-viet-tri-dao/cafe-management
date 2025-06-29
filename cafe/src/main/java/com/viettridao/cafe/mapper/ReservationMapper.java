package com.viettridao.cafe.mapper;

import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.request.tables.ReservationRequest;
import com.viettridao.cafe.dto.response.tables.ReservationResponse;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.model.ReservationKey;
import com.viettridao.cafe.model.TableEntity;

@Component
public class ReservationMapper {

    public ReservationEntity fromRequest(ReservationRequest request, TableEntity table, EmployeeEntity employee,
                                         InvoiceEntity invoice) {
        ReservationEntity entity = new ReservationEntity();

        ReservationKey key = new ReservationKey();
        key.setIdTable(table.getId());
        key.setIdEmployee(employee.getId());
        key.setIdInvoice(invoice.getId());
        entity.setId(key);

        entity.setTable(table);
        entity.setEmployee(employee);
        entity.setInvoice(invoice);
        entity.setCustomerName(request.getCustomerName());
        entity.setCustomerPhone(request.getCustomerPhone());
        entity.setReservationDate(request.getReservationDate());

        // ✅ Lưu giờ đặt
        entity.setReservationTime(request.getReservationTime());

        entity.setIsDeleted(false);
        return entity;
    }

    public ReservationResponse toResponse(ReservationEntity entity) {
        ReservationResponse response = new ReservationResponse();
        response.setCustomerName(entity.getCustomerName());
        response.setCustomerPhone(entity.getCustomerPhone());
        response.setReservationDate(entity.getReservationDate());

        // ✅ Lấy giờ đặt
        response.setReservationTime(entity.getReservationTime());

        return response;
    }
}
