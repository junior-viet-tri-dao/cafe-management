package com.viettridao.cafe.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.EquipmentEntity;

@Repository
public interface EquipmentRepository extends JpaRepository<EquipmentEntity, Integer> {
    List<EquipmentEntity> findAllByDeletedFalse();

    List<EquipmentEntity> findByPurchaseDateBetweenAndDeletedFalse(LocalDate start, LocalDate end);
}