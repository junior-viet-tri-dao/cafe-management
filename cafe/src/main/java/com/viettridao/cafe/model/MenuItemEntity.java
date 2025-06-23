package com.viettridao.cafe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "menu_items")//thucdon
public class MenuItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_item_id")
    private Integer id;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "current_price")
    private Double currentPrice;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
