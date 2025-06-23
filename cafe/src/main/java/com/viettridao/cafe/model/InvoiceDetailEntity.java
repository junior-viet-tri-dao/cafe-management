package com.viettridao.cafe.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "invoice_details")
public class InvoiceDetailEntity {
    @EmbeddedId
    private InvoiceKey id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price_at_sale_time")
    private Double price;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
