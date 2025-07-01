package com.viettridao.cafe.dto.response.menu;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuItemResponse {
    private Integer id;
    private String itemName;
    private Double currentPrice;
    private List<MenuDetailResponse> ingredients;
}

