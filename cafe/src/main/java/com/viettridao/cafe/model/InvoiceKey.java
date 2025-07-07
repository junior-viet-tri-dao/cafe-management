package com.viettridao.cafe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

/**
 * Khóa chính cho thực thể InvoiceDetail.
 */
@Getter
@Setter
@Embeddable
/**
 * Composite key cho InvoiceDetailEntity (invoice_id, menu_item_id)
 */
public class InvoiceKey {
    @Column(name = "invoice_id") // ID của hóa đơn
    private Integer idInvoice;

    @Column(name = "menu_item_id") // ID của món ăn
    private Integer idMenuItem;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        InvoiceKey that = (InvoiceKey) obj;
        return java.util.Objects.equals(idInvoice, that.idInvoice) &&
                java.util.Objects.equals(idMenuItem, that.idMenuItem);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(idInvoice, idMenuItem);
    }
}
