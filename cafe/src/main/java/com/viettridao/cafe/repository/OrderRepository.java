package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
    List<OrderEntity> findAllByTable_IdAndIsDeletedFalse(Integer tableId);

    // Tìm order chưa thanh toán của bàn (giả sử chỉ lấy order đầu tiên chưa thanh
    // toán)
    Optional<OrderEntity> findFirstByTable_IdAndIsDeletedFalse(Integer tableId);
}
