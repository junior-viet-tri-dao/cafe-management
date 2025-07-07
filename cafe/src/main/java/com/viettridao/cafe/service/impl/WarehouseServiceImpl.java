package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.response.warehouse.WarehousePageResponse;
import com.viettridao.cafe.dto.response.warehouse.WarehouseResponse;
import com.viettridao.cafe.repository.ExportRepository;
import com.viettridao.cafe.repository.ImportRepository;
import com.viettridao.cafe.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Lớp triển khai các phương thức xử lý logic liên quan đến kho hàng.
 */
@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    // Repository quản lý dữ liệu nhập kho
    private final ImportRepository importRepository;

    // Repository quản lý dữ liệu xuất kho
    private final ExportRepository exportRepository;

    /**
     * Lấy danh sách tất cả các kho hàng theo từ khóa tìm kiếm và phân trang.
     *
     * @param keyword Từ khóa tìm kiếm.
     * @param page    Số trang cần lấy.
     * @param size    Số lượng phần tử trên mỗi trang.
     * @return Đối tượng WarehousePageResponse chứa thông tin danh sách kho hàng.
     */
    @Override
    public WarehousePageResponse getAllWarehouses(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WarehouseResponse> wareHouseResponses = importRepository.getAllWarehouses(keyword, pageable);

        WarehousePageResponse response = new WarehousePageResponse();
        response.setPageNumber(wareHouseResponses.getNumber());
        response.setPageSize(wareHouseResponses.getSize());
        response.setTotalElements(wareHouseResponses.getTotalElements());
        response.setTotalPages(wareHouseResponses.getTotalPages());
        response.setWarehouses(wareHouseResponses.getContent());

        return response;
    }
}
