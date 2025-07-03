package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.EquipmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<EquipmentEntity, Integer> {
    @Query("select e from EquipmentEntity e where e.isDeleted = false")
    List<EquipmentEntity> getAllEquipments();

    @Query("select e from EquipmentEntity e where e.isDeleted = false")
    Page<EquipmentEntity> getAllEquipmentsByPage(Pageable pageable);

    List<EquipmentEntity> findByPurchaseDateBetweenAndIsDeletedFalse(LocalDate start, LocalDate end);
}
