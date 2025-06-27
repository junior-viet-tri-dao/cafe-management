package com.viettridao.cafe.dto.response.menu_item;

import lombok.Data;

@Data
public class MenuDetailResponse {
    private Integer productId;
    private String productName;
    private Double quantity;
    private String unitName;
}
