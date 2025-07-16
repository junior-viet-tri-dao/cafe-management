package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.PromotionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<PromotionEntity, Integer> {

    @Query("Select p from PromotionEntity p where p.isDeleted = false")
    List<PromotionEntity> getAllPromotion();

    @Query("Select p from PromotionEntity p where p.isDeleted = false and LOWER(p.promotionName) like lower(CONCAT('%', :namePromotion, '%'))")
    Page<PromotionEntity> getAllPromotionPageSearch(@Param("namePromotion") String namePromotion, Pageable pageable);

    @Query("Select p from PromotionEntity p where p.isDeleted = false ")
    Page<PromotionEntity> getAllPromotionPage(Pageable pageable);
}
