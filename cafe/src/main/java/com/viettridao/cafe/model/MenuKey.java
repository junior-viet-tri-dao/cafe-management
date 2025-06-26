package com.viettridao.cafe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class MenuKey {
    @Column(name = "product_id")
    private Integer idProduct;

    @Column(name = "menu_item_id")
    private Integer idMenuItem;
}
