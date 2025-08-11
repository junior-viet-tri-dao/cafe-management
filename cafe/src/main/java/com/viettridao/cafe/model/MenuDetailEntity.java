package com.viettridao.cafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "menu_item_ingredients")
public class MenuDetailEntity {
    @EmbeddedId
    private MenuKey id;

    @NotNull(message = "Sản phẩm không được để trống")
    @ManyToOne
    @MapsId("idProduct")
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @NotNull(message = "Mục menu không được để trống")
    @ManyToOne
    @MapsId("idMenuItem")
    @JoinColumn(name = "menu_item_id")
    private MenuItemEntity menuItem;

    @NotNull(message = "Số lượng không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Số lượng phải lớn hơn 0")
    @Column(name = "quantity")
    private Double quantity;

    @NotBlank(message = "Đơn vị tính không được để trống")
    @Size(max = 20, message = "Đơn vị tính không được vượt quá 20 ký tự")
    @Column(name = "unit")
    private String unitName;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
