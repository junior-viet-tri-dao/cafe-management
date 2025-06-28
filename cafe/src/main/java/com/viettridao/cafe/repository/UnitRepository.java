package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.UnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitRepository extends JpaRepository<UnitEntity, Integer> {
    List<UnitEntity> findAllByIsDeleted(Boolean isDeleted);
}
