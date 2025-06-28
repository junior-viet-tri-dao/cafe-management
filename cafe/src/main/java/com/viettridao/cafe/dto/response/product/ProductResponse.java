package com.viettridao.cafe.dto.response.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {
    private Integer id;

    private String productName;

    private Integer quantity;

    private Double productPrice;

    private String unitName;
}
