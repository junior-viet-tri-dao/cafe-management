package com.viettridao.cafe.service;

// Import các thư viện cần thiết
import com.viettridao.cafe.dto.response.warehouse.WarehousePageResponse;

/**
 * Service cho quản lý kho (Warehouse).
 * Chịu trách nhiệm xử lý logic nghiệp vụ liên quan đến kho hàng.
 */
public interface WarehouseService {

    /**
     * Lấy danh sách tất cả các kho hàng theo từ khóa tìm kiếm và phân trang.
     *
     * @param keyword Từ khóa tìm kiếm theo tên kho hàng.
     * @param page    Số trang cần lấy.
     * @param size    Số lượng bản ghi trên mỗi trang.
     * @return Đối tượng WarehousePageResponse chứa danh sách kho hàng và thông tin
     *         phân trang.
     */
    WarehousePageResponse getAllWarehouses(String keyword, int page, int size);
}
