package com.viettridao.cafe.dto.response.menu;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuItemDetailResponse {
    private Integer productId;

    private String unitName;

    private Double quantity;
}