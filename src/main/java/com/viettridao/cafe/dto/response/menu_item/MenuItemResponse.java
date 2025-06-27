package com.viettridao.cafe.dto.response.menu_item;

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
    private Double currentPrice;
    private Boolean isDeleted;
    private List<MenuDetailResponse> menuDetails;
}
