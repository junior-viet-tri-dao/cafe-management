package com.viettridao.cafe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.UnitEntity;

@Repository
public interface UnitRepository extends JpaRepository<UnitEntity, Integer> {

	List<UnitEntity> findByIsDeletedFalse();

	Optional<UnitEntity> findByUnitNameIgnoreCaseAndIsDeletedFalse(String unitName);

	List<UnitEntity> findAllByIsDeletedFalse();

	Optional<UnitEntity> findByIdAndIsDeletedFalse(Integer id);

}
