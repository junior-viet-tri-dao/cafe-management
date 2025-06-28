package com.viettridao.cafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.UnitEntity;

@Repository
public interface UnitRepository extends JpaRepository<UnitEntity, Integer> {

    List<UnitEntity> findByDeletedFalse();
}
