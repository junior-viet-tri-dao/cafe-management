package com.viettridao.cafe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
/**
 * Composite key cho MenuDetailEntity (product_id, menu_item_id)
 */
public class MenuKey {
    @Column(name = "product_id")
    private Integer idProduct; // ID của sản phẩm liên kết với chi tiết menu

    @Column(name = "menu_item_id")
    private Integer idMenuItem; // ID của món ăn liên kết với chi tiết menu

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        MenuKey that = (MenuKey) obj;
        return java.util.Objects.equals(idProduct, that.idProduct) &&
                java.util.Objects.equals(idMenuItem, that.idMenuItem);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(idProduct, idMenuItem);
    }
}
