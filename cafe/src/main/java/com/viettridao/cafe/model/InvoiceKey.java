package com.viettridao.cafe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceKey {
    @NotNull(message = "ID hóa đơn không được để trống")
    @Column(name = "invoice_id")
    private Integer idInvoice;

    @NotNull(message = "ID món ăn không được để trống")
    @Column(name = "menu_item_id")
    private Integer idMenuItem;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceKey that = (InvoiceKey) o;
        return Objects.equals(idInvoice, that.idInvoice) && Objects.equals(idMenuItem, that.idMenuItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idInvoice, idMenuItem);
    }
}
