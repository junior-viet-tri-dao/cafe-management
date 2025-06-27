package com.viettridao.cafe.dto.response.product;

import com.viettridao.cafe.model.UnitEntity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ProductResponse {
    private Integer id;
    private String productName;
    private Integer quantity;
    private Boolean isDeleted;
    private Double price;
    private UnitEntity unit;

}
