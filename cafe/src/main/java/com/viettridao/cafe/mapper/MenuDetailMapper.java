package com.viettridao.cafe.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.viettridao.cafe.dto.request.menudetail.MenuDetailCreateRequest;
import com.viettridao.cafe.dto.response.menudetail.MenuDetailResponse;
import com.viettridao.cafe.model.MenuDetailEntity;
import com.viettridao.cafe.model.MenuItemEntity;
import com.viettridao.cafe.model.ProductEntity;

@Mapper(componentModel = "spring")
public interface MenuDetailMapper {

    @Mapping(target = "id", expression = "java(new com.viettridao.cafe.model.MenuKey(detailRequest.getProductId(), menuItem.getId()))")
    @Mapping(target = "product", source = "detailRequest.productId", qualifiedByName = "mapProductId")
    @Mapping(target = "menuItem", source = "menuItem")
    @Mapping(target = "deleted", constant = "false")
    MenuDetailEntity toEntity(MenuDetailCreateRequest detailRequest, MenuItemEntity menuItem);

    MenuDetailResponse toResponse(MenuDetailEntity entity);

    @Named("mapProductId")
    default ProductEntity mapProductId(Integer productId) {
        if (productId == null) return null;
        ProductEntity product = new ProductEntity();
        product.setId(productId);
        return product;
    }

//    @Named("mapMenuItemId")
//    default MenuItemEntity mapMenuItemId(Integer menuItemId) {
//        if (menuItemId == null) return null;
//        MenuItemEntity menuItem = new MenuItemEntity();
//        menuItem.setId(menuItemId);
//        return menuItem;
//    }
}
