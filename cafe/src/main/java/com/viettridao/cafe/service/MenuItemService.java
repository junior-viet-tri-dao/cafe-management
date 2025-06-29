package com.viettridao.cafe.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.viettridao.cafe.dto.response.menu.MenuItemResponse;

public interface MenuItemService {

	// Tạo món mới
	MenuItemResponse create(MenuItemRequest request);

	// Cập nhật món đã tồn tại
	MenuItemResponse update(Integer id, MenuItemRequest request);

	// Xóa mềm món
	void delete(Integer id);

	// Lấy 1 món theo ID
	MenuItemResponse getById(Integer id);

	// Lấy danh sách tất cả món (không phân trang)
	List<MenuItemResponse> getAll();

	// Lấy danh sách theo keyword tìm kiếm (không phân trang)
	List<MenuItemResponse> search(String keyword);

	// Danh sách tất cả món có phân trang
	Page<MenuItemResponse> getAllPaged(int page, int size);

	// Tìm kiếm món có phân trang
	Page<MenuItemResponse> searchPaged(String keyword, int page, int size);
}
