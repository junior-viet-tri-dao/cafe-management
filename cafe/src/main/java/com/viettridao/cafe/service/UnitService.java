package com.viettridao.cafe.service;

// Import các thư viện cần thiết
import com.viettridao.cafe.model.UnitEntity;

import java.util.List;

/**
 * Service cho thực thể UnitEntity.
 * Chịu trách nhiệm xử lý logic nghiệp vụ liên quan đến đơn vị (Unit).
 */
public interface UnitService {

    /**
     * Lấy danh sách tất cả các đơn vị.
     *
     * @return Danh sách các thực thể UnitEntity.
     */
    List<UnitEntity> getAllUnits();

    /**
     * Lấy thông tin chi tiết của một đơn vị dựa trên ID.
     *
     * @param id ID của đơn vị cần lấy thông tin.
     * @return Thực thể UnitEntity tương ứng với ID.
     */
    UnitEntity getUnitById(Integer id);
}
