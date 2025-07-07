package com.viettridao.cafe.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.viettridao.cafe.dto.response.sales.MenuItemResponse;
import com.viettridao.cafe.model.MenuItemEntity;

public class MenuItemMapper {
    public static MenuItemResponse toMenuItemResponse(MenuItemEntity entity) {
        if (entity == null)
            return null;
        MenuItemResponse dto = new MenuItemResponse();
        dto.setId(entity.getId());
        dto.setItemName(entity.getItemName());
        dto.setPrice(entity.getCurrentPrice());
        return dto;
    }

    public static List<MenuItemResponse> toMenuItemResponseList(List<MenuItemEntity> entities) {
        if (entities == null)
            return null;
        return entities.stream().map(MenuItemMapper::toMenuItemResponse).collect(Collectors.toList());
    }
}
