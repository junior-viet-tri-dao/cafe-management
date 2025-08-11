package com.viettridao.cafe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class ReservationKey {
    @NotNull(message = "ID bàn không được để trống")
    @Column(name = "table_id")
    private Integer idTable;

    @NotNull(message = "ID nhân viên không được để trống")
    @Column(name = "employee_id")
    private Integer idEmployee;

    @NotNull(message = "ID hóa đơn không được để trống")
    @Column(name = "invoice_id")
    private Integer idInvoice;
}
