package com.viettridao.cafe.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.response.menu_item.MenuItemResponse;
import com.viettridao.cafe.model.MenuItemEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MenuItemMapper {
    private final ModelMapper modelMapper;

    public MenuItemResponse convertToDto(MenuItemEntity menuItemEntity) {
        return modelMapper.map(menuItemEntity, MenuItemResponse.class);
    }

    // public MenuItemEntity convertToEntity(CreateMenuItemRequest request) {
    // return modelMapper.map(request, MenuItemEntity.class);
    // }
}
