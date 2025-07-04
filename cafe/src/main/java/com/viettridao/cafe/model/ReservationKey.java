package com.viettridao.cafe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class ReservationKey {
    @Column(name = "table_id")
    private Integer idTable;

    @Column(name = "employee_id")
    private Integer idEmployee;

    @Column(name = "invoice_id")
    private Integer idInvoice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationKey that = (ReservationKey) o;
        return Objects.equals(idTable, that.idTable) && Objects.equals(idEmployee, that.idEmployee) && Objects.equals(idInvoice, that.idInvoice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTable, idEmployee, idInvoice);
    }
}
