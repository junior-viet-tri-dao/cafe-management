package com.viettridao.cafe.dto.response.invoice;

import com.viettridao.cafe.dto.response.menu_item.MenuItemResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceDetailResponse {
    private Integer menuItemId;
    private MenuItemResponse menuItem;
    private Integer quantity;
    private Double price;
    private Boolean isDeleted;
}
