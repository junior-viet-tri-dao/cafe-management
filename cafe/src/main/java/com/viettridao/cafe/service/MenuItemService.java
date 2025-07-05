package com.viettridao.cafe.service;

import org.springframework.data.domain.Page;

import com.viettridao.cafe.dto.request.menu.MenuItemRequest;
import com.viettridao.cafe.dto.response.menu.MenuItemResponse;

public interface MenuItemService {

	void add(MenuItemRequest request);

	void update(Integer id, MenuItemRequest request);

	void delete(Integer id);

	MenuItemResponse getById(Integer id);

	MenuItemResponse getByName(String name);

	Page<MenuItemResponse> getAll(String keyword, int page, int size);
}
