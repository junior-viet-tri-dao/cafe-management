package com.viettridao.cafe.repository;

import java.time.LocalDate;
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
	@Query("select e from EquipmentEntity e where e.isDeleted = false")
	List<EquipmentEntity> getAllEquipments();

	@Query("select e from EquipmentEntity e where e.isDeleted = false")
	Page<EquipmentEntity> getAllEquipmentsByPage(Pageable pageable);

	@Query("SELECT e FROM EquipmentEntity e WHERE e.purchaseDate BETWEEN :from AND :to AND e.isDeleted = false")
	List<EquipmentEntity> findEquipmentsBetweenDates(@Param("from") LocalDate from, @Param("to") LocalDate to);
}
