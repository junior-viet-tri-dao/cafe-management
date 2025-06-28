package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.menuitem.MenuItemCreateRequest;
import com.viettridao.cafe.dto.request.menuitem.MenuItemUpdateRequest;
import com.viettridao.cafe.dto.response.menuitem.MenuItemPageResponse;
import com.viettridao.cafe.model.MenuItemEntity;

public interface MenuItemService {
    MenuItemPageResponse getAllMenuItems(String keyword, int page, int size);
    MenuItemEntity createMenuItem(MenuItemCreateRequest request);
    void updateMenuItem(MenuItemUpdateRequest request);
    void deleteMenuItem(Integer id);
    MenuItemEntity getMenuItemById(Integer id);
}
