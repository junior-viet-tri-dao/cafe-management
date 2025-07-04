package com.viettridao.cafe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@Embeddable
public class MenuKey {
    @Column(name = "product_id")
    private Integer idProduct;

    @Column(name = "menu_item_id")
    private Integer idMenuItem;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuKey menuKey = (MenuKey) o;
        return Objects.equals(idProduct, menuKey.idProduct) && Objects.equals(idMenuItem, menuKey.idMenuItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProduct, idMenuItem);
    }
}
