package com.viettridao.cafe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class MenuKey {
    @NotNull(message = "ID sản phẩm không được để trống")
    @Column(name = "product_id")
    private Integer idProduct;

    @NotNull(message = "ID mục menu không được để trống")
    @Column(name = "menu_item_id")
    private Integer idMenuItem;
}
