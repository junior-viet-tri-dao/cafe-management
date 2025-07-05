package com.viettridao.cafe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "invoice_details")
public class InvoiceDetailEntity {
    @EmbeddedId
    private InvoiceKey id;

    @ManyToOne
    @MapsId("idInvoice")
    @JoinColumn(name = "invoice_id")
    private InvoiceEntity invoice;

    @ManyToOne
    @MapsId("idMenuItem")
    @JoinColumn(name = "menu_item_id")
    private MenuItemEntity menuItem;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price_at_sale_time")
    private Double price;

    @Column(name = "is_deleted")
    private Boolean deleted;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceDetailEntity that = (InvoiceDetailEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(invoice, that.invoice) && Objects.equals(menuItem, that.menuItem) && Objects.equals(quantity, that.quantity) && Objects.equals(price, that.price) && Objects.equals(deleted, that.deleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, invoice, menuItem, quantity, price, deleted);
    }
}
