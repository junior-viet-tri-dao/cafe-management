package com.viettridao.cafe.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    @Query("""
            SELECT p
            FROM ProductEntity p
            WHERE LOWER(p.productName) LIKE CONCAT('%', LOWER(:keyword), '%')
            AND p.isDeleted = false
            """)
    Page<ProductEntity> searchByName(Pageable pageable, @Param("keyword") String keyword);

    Page<ProductEntity> findAllByIsDeletedFalse(Pageable pageable);

    List<ProductEntity> findAllByIsDeletedFalse();
}
