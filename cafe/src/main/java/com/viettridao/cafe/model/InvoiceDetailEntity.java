package com.viettridao.cafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "invoice_details")
public class InvoiceDetailEntity {
    @EmbeddedId
    private InvoiceKey id;

    @NotNull(message = "Hóa đơn không được để trống")
    @ManyToOne
    @MapsId("idInvoice")
    @JoinColumn(name = "invoice_id")
    private InvoiceEntity invoice;

    @NotNull(message = "Món ăn không được để trống")
    @ManyToOne
    @MapsId("idMenuItem")
    @JoinColumn(name = "menu_item_id")
    private MenuItemEntity menuItem;


    @Column(name = "quantity")
    private Integer quantity;

    @NotNull(message = "Giá tại thời điểm bán không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Giá không thể là số âm")
    @Column(name = "price_at_sale_time")
    private Double price;


    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
