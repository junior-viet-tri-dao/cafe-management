package com.viettridao.cafe.dto.response.menuitem;

import com.viettridao.cafe.dto.response.menuitem_detail.MenuItemDetailResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuItemResponse {
    private Integer id;

    private String itemName;

    private Double currentPrice;

    private List<MenuItemDetailResponse> details;

    private String detailsJson;
}
