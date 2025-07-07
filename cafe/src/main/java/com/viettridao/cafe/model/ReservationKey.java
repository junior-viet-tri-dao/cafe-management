package com.viettridao.cafe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
/**
 * Composite key cho ReservationEntity (table_id, employee_id, invoice_id)
 */
public class ReservationKey {
    @Column(name = "table_id")
    private Integer idTable; // ID của bàn liên kết với đặt chỗ

    @Column(name = "employee_id")
    private Integer idEmployee; // ID của nhân viên liên kết với đặt chỗ

    @Column(name = "invoice_id")
    private Integer idInvoice; // ID của hóa đơn liên kết với đặt chỗ

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        ReservationKey that = (ReservationKey) obj;
        return java.util.Objects.equals(idTable, that.idTable) &&
                java.util.Objects.equals(idEmployee, that.idEmployee) &&
                java.util.Objects.equals(idInvoice, that.idInvoice);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(idTable, idEmployee, idInvoice);
    }
}
