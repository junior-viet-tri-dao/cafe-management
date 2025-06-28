package com.viettridao.cafe.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.viettridao.cafe.dto.request.menuitem.MenuItemCreateRequest;
import com.viettridao.cafe.dto.request.menuitem.MenuItemUpdateRequest;
import com.viettridao.cafe.dto.response.menuitem.MenuItemResponse;
import com.viettridao.cafe.model.MenuItemEntity;

@Mapper(componentModel = "spring")
public interface MenuItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "menuDetails", ignore = true)
    @Mapping(target = "invoiceDetails", ignore = true)
    MenuItemEntity toEntity(MenuItemCreateRequest request);

    MenuItemResponse toResponse(MenuItemEntity entity);

    MenuItemUpdateRequest toUpdateRequest(MenuItemEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateEntityFromRequest(MenuItemUpdateRequest request, @MappingTarget MenuItemEntity entity);
}
