package com.viettridao.cafe.dto.request.menuitem;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.viettridao.cafe.dto.request.menudetail.MenuDetailUpdateRequest;


@Getter
@Setter
public class MenuItemUpdateRequest {

    private Integer id;

    private String itemName;

    private Double currentPrice;

    private List<MenuDetailUpdateRequest> menuDetails;
}
