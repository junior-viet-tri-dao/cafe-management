package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.menuitem.MenuItemCreateRequest;
import com.viettridao.cafe.dto.request.menuitem.MenuItemUpdateRequest;
import com.viettridao.cafe.dto.response.menuitem.MenuItemPageResponse;
import com.viettridao.cafe.model.MenuItemEntity;

import java.util.List;

public interface MenuItemService {
    MenuItemPageResponse getAllMenuItems(String keyword, int page, int size);
    MenuItemEntity createMenuItem(MenuItemCreateRequest request);
    void updateMenuItem(MenuItemUpdateRequest request);
    void deleteMenuItem(Integer id);
    MenuItemEntity getMenuItemById(Integer id);
    List<MenuItemEntity> getAllMenuItems();
}
