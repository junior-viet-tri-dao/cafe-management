package com.viettridao.cafe.service;

import java.util.List;

import com.viettridao.cafe.dto.request.menu_item.MenuItemRequest;
import com.viettridao.cafe.dto.response.menu_item.MenuItemResponse;

public interface MenuItemService {
    List<MenuItemResponse> getAllMenuItems();

    void deleteMenuItem(Integer id);

    void createMenu(MenuItemRequest request);

    MenuItemResponse getMenuItemById(Integer id);

    void updateMenuItem(Integer id, MenuItemRequest request);

    List<MenuItemResponse> searchMenuItemsByName(String trim);
}
