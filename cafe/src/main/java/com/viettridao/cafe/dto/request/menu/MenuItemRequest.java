package com.viettridao.cafe.dto.request.menu;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuItemRequest {
    private String itemName;
    private Double currentPrice;
    private List<MenuDetailRequest> ingredients;
}
