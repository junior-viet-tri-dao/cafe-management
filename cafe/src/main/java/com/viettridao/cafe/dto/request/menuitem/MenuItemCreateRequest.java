package com.viettridao.cafe.dto.request.menuitem;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.viettridao.cafe.dto.request.menudetail.MenuDetailCreateRequest;

@Getter
@Setter
public class MenuItemCreateRequest {

    private String itemName;

    private Double currentPrice;

    private List<MenuDetailCreateRequest> menuDetails;
}
