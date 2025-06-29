package com.viettridao.cafe.dto.response.tables;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationResponse {
	private String customerName;
	private String customerPhone;
	private LocalDate reservationDate;
	private LocalTime reservationTime;
}
