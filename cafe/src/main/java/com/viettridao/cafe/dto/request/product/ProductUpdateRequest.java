package com.viettridao.cafe.dto.request.product;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductUpdateRequest {

    private Integer id;

    private String productName;

    private Integer quantity;

    private Double price;

    private Integer unitId;
}
