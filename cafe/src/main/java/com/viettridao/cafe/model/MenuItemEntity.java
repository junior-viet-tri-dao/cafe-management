package com.viettridao.cafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "menu_items")//thucdon
public class MenuItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_item_id")
    private Integer id;

    @NotBlank(message = "Tên món ăn không được để trống")
    @Size(max = 100, message = "Tên món ăn không được vượt quá 100 ký tự")
    @Column(name = "item_name")
    private String itemName;

    @NotNull(message = "Giá hiện tại không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Giá không thể là số âm")
    @Column(name = "current_price")
    private Double currentPrice;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL)
    private List<MenuDetailEntity> menuDetails;

    @OneToMany(mappedBy = "menuItem", cascade = CascadeType.ALL)
    private List<InvoiceDetailEntity> invoiceDetails;
}
