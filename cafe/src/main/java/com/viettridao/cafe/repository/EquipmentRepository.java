package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.EquipmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<EquipmentEntity, Integer> {
    List<EquipmentEntity> findAllByIsDeletedFalse();

    Page<EquipmentEntity> findAllByIsDeletedFalse(Pageable pageable);
}
