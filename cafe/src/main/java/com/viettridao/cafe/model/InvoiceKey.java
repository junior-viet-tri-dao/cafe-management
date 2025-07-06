package com.viettridao.cafe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class InvoiceKey {
    @Column(name = "invoice_id")
    private Integer idInvoice;

    @Column(name = "menu_item_id")
    private Integer idMenuItem;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceKey that = (InvoiceKey) o;
        return Objects.equals(idInvoice, that.idInvoice) && Objects.equals(idMenuItem, that.idMenuItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idInvoice, idMenuItem);
    }
}
