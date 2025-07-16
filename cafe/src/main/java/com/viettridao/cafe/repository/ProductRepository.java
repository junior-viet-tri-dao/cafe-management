package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.model.PromotionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

    @Query("Select p from ProductEntity p where p.isDeleted = false and p.id = :id")
    List<ProductEntity> getAllProduct(Integer id);

    @Query("Select p from ProductEntity p where p.isDeleted = false and LOWER(p.productName) like lower(CONCAT('%', :keyword, '%'))")
    Page<ProductEntity> getAllProductPageSearch(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "Select p from ProductEntity p where p.isDeleted = false ")
    Page<ProductEntity> getAllProductPage(Pageable pageable);



}
