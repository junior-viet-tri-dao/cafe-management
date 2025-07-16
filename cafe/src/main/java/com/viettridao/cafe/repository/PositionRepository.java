package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.AccountEntity;
import com.viettridao.cafe.model.PositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<PositionEntity, Integer> {
//    @Query("select a from AccountEntity a where a.isDeleted = false and a.id = '1'")
//    Optional<AccountEntity> findById(Integer id);

    @Query("select p from PositionEntity p where p.isDeleted = false")
    List<PositionEntity> getAllPosition();





}
