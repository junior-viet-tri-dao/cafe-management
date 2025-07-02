package com.viettridao.cafe.dto.request.tables;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuItemSplitWrapper {
    private List<MenuItemSplitRequest> items;
}

