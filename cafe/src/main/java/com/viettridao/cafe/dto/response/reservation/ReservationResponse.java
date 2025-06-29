package com.viettridao.cafe.dto.response.reservation;

import lombok.Getter;
import lombok.Setter;
import java.time.*;

@Getter
@Setter
public class ReservationResponse {

    private Integer tableId;
    private String tableName;
    private Integer employeeId;
    private String employeeName;
    private Integer invoiceId;
    private String customerName;
    private String customerPhone;
    private LocalDate reservationDate;
}
