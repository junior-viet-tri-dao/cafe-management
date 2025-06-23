package com.viettridao.cafe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class InvoiceKey {
    @Column(name = "invoice_id")
    private Integer idInvoice;

    @Column(name = "menu_item_id")
    private Integer idMenuItem;
}
