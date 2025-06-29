package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.PositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository<PositionEntity, Integer> {
    List<PositionEntity> findAllByIsDeletedFalse();
}
