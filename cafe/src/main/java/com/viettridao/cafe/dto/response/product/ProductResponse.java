package com.viettridao.cafe.dto.response.product;

import lombok.Getter;
import lombok.Setter;
import java.time.*;

import com.viettridao.cafe.dto.response.unit.UnitResponse;

@Getter
@Setter
public class ProductResponse {

    private Integer id;

    private String productName;

    private Integer quantity;

    private Double price;

    private UnitResponse unit;
}
