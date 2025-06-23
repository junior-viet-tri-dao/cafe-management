package com.viettridao.cafe.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "menu_item_ingredients")
public class MenuDetailEntity {
    @EmbeddedId
    private MenuKey id;

    @Column(name = "quantity")
    private Double quantity;

    @Column(name = "unit")
    private String unitName;

    @Column(name = "is_deleted")
    private Boolean isDeleted;
}
