package com.viettridao.cafe.model;

import java.io.Serializable;

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
public class MenuKey implements Serializable {
    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "menu_item_id")
    private Integer menuId; // Changed from menuId to menuItemId for clarity
}
