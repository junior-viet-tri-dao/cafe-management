package com.viettridao.cafe.dto.response.reservation;

import java.time.LocalDate;

import com.viettridao.cafe.model.ReservationKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationResponse {
    private ReservationKey id;
    private String customerName;
    private String customerPhone;
    private LocalDate reservationDate;
    private Boolean isDeleted;
}
