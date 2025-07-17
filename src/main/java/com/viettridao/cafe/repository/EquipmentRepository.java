package com.viettridao.cafe.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.EquipmentEntity;

@Repository
public interface EquipmentRepository extends JpaRepository<EquipmentEntity, Integer> {

    @Query("""
            SELECT e
            FROM EquipmentEntity e
            WHERE LOWER(e.equipmentName) LIKE CONCAT('%', LOWER(:keyword), '%')
            AND e.isDeleted = false
            """)
    Page<EquipmentEntity> searchByName(Pageable pageable, @Param("keyword") String keyword);

    Page<EquipmentEntity> findAllByIsDeletedFalse(Pageable pageable);

}
