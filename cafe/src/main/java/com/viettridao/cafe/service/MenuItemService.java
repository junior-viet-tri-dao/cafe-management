package com.viettridao.cafe.service;

import java.util.List;

import com.viettridao.cafe.dto.request.menu_item.CreateMenuItemRequest;
import com.viettridao.cafe.dto.request.menu_item.UpdateMenuItemRequest;
import com.viettridao.cafe.dto.response.menu_item.MenuItemPageResponse;
import com.viettridao.cafe.dto.response.menu_item.MenuItemResponse;
import com.viettridao.cafe.model.MenuItemEntity;

public interface MenuItemService {
    MenuItemPageResponse getAllMenuItems(String keyword, int page, int size);
    MenuItemEntity createMenuItem(CreateMenuItemRequest request);
    void updateMenuItem(UpdateMenuItemRequest request);
    void deleteMenuItem(Integer id);
    MenuItemEntity getMenuItemById(Integer id);
    boolean existsByItemName(String itemName);
    boolean existsByItemNameAndIdNot(String itemName, Integer menuItemId);
}