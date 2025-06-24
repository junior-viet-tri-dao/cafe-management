package com.viettridao.cafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.PositionEntity;

@Repository
public interface PositionRepository extends JpaRepository<PositionEntity, Integer> {
    // Các phương thức truy vấn tùy chỉnh có thể được định nghĩa ở đây

}
