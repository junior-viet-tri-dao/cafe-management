package com.viettridao.cafe.dto.request.product;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreateRequest {

    private String productName;

    private Integer quantity = 0;

    private Double price;

    private Integer unitId;

}
