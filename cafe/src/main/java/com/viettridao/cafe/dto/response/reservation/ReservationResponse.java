package com.viettridao.cafe.dto.response.reservation;

import lombok.Getter;
import lombok.Setter;

import java.time.*;

import com.viettridao.cafe.model.ReservationKey;

@Getter
@Setter
public class ReservationResponse {

    private ReservationKey reservationKey;

    private String tableName;

    private String employeeName;

    private String customerName;

    private String customerPhone;

    private LocalDateTime reservationDate;
}
