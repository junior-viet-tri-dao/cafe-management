package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.model.UnitEntity;
import com.viettridao.cafe.repository.UnitRepository;
import com.viettridao.cafe.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Lớp triển khai các phương thức xử lý logic liên quan đến đơn vị tính.
 */
@Service
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService {

    // Repository quản lý dữ liệu đơn vị tính
    private final UnitRepository unitRepository;

    /**
     * Lấy danh sách tất cả các đơn vị tính chưa bị xóa.
     *
     * @return Danh sách các đối tượng UnitEntity.
     */
    @Override
    public List<UnitEntity> getAllUnits() {
        return unitRepository.findAllByIsDeleted(false);
    }

    /**
     * Lấy thông tin đơn vị tính theo ID.
     *
     * @param id ID của đơn vị tính cần tìm.
     * @return Đối tượng UnitEntity tương ứng với ID.
     * @throws RuntimeException nếu không tìm thấy đơn vị tính với ID đã cho.
     */
    @Override
    public UnitEntity getUnitById(Integer id) {
        return unitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn vị tính có id=" + id));
    }
}
