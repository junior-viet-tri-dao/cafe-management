package com.viettridao.cafe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

	List<ProductEntity> findAllByIsDeletedFalse();

	Page<ProductEntity> findAllByIsDeletedFalse(Pageable pageable);

	Optional<ProductEntity> findByIdAndIsDeletedFalse(Integer id);

	Optional<ProductEntity> findByProductNameIgnoreCase(String productName);

	List<ProductEntity> findByProductNameContainingIgnoreCaseAndIsDeletedFalse(String keyword);

	Page<ProductEntity> findByProductNameContainingIgnoreCaseAndIsDeletedFalse(String keyword, Pageable pageable);
}
