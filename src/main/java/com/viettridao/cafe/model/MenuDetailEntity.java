package com.viettridao.cafe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "menu_item_ingredients")
public class MenuDetailEntity {
    @EmbeddedId
    private MenuKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("menuId")
    @JoinColumn(name = "menu_item_id")
    private MenuItemEntity menuItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Column(name = "quantity")
    private Double quantity;

    @Column(name = "unit")
    private String unitName;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
