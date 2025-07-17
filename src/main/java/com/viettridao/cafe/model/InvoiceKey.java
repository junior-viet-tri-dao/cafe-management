package com.viettridao.cafe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceKey {
    @Column(name = "invoice_id")
    private Integer idInvoice;

    @Column(name = "menu_item_id")
    private Integer idMenuItem;
}
