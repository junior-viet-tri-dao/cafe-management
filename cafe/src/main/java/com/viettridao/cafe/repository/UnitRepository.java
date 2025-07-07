package com.viettridao.cafe.repository;

// Import các thư viện cần thiết
import com.viettridao.cafe.model.UnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository cho thực thể UnitEntity.
 * Chịu trách nhiệm truy vấn dữ liệu liên quan đến đơn vị (Unit) từ cơ sở dữ liệu
 */
@Repository
public interface UnitRepository extends JpaRepository<UnitEntity, Integer> {

    /**
     * Lấy danh sách tất cả các đơn vị dựa trên trạng thái xóa.
     *
     * @param isDeleted Trạng thái xóa của đơn vị (true: đã xóa, false: chưa xóa).
     * @return Danh sách các thực thể UnitEntity theo trạng thái xóa.
     */
    List<UnitEntity> findAllByIsDeleted(Boolean isDeleted);
}
