package com.viettridao.cafe.dto.request.tables;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationRequest {
    private Integer tableId;
    private Integer employeeId;
    private String customerName;
    private String customerPhone;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
}


