package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.PromotionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository extends JpaRepository<PromotionEntity, Integer> {
    @Query("select p from PromotionEntity p where p.isDeleted = false ")
    Page<PromotionEntity> getAllByPromotions(Pageable pageable);
}
