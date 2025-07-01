package com.viettridao.cafe.service;

import org.springframework.data.domain.Page;

import com.viettridao.cafe.dto.request.menu.MenuItemRequest;
import com.viettridao.cafe.dto.response.menu.MenuItemResponse;

public interface MenuItemService {

    // ✅ Thêm món mới
    void add(MenuItemRequest request);

    // ✅ Cập nhật món theo ID
    void update(Integer id, MenuItemRequest request);

    // ✅ Xóa mềm món
    void delete(Integer id);

    // ✅ Lấy món theo ID
    MenuItemResponse getById(Integer id);

    // ✅ Tìm món theo tên chính xác (không phân biệt hoa thường)
    MenuItemResponse getByName(String name);

    // ✅ Lấy danh sách món theo từ khóa (phân trang)
    Page<MenuItemResponse> getAll(String keyword, int page, int size);
}
