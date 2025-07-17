package com.viettridao.cafe.dto.response.menu_item;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class MenuItemResponse {
    private Integer id;
    private String itemName;
    private BigDecimal currentPrice;
    private Boolean isDeleted;
    private List<MenuDetailResponse> menuDetails;
}
