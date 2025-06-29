package com.viettridao.cafe.dto.response.tables;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableOrderDetailResponse {
    private String menuItemName;
    private Integer quantity;
}
