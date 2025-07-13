package com.viettridao.cafe.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.ImportEntity;

@Repository
public interface ImportRepository extends JpaRepository<ImportEntity, Integer> {

	List<ImportEntity> findByIsDeletedFalse();

	List<ImportEntity> findByProductIdAndIsDeletedFalse(Integer productId);

	Page<ImportEntity> findByProductIdAndIsDeletedFalse(Integer productId, Pageable pageable);

	Page<ImportEntity> findByProduct_ProductNameContainingIgnoreCaseAndIsDeletedFalse(String keyword,
			Pageable pageable);

	Optional<ImportEntity> findTopByProductIdOrderByImportDateDesc(Integer productId);

	@Query("SELECT i FROM ImportEntity i JOIN FETCH i.product WHERE i.isDeleted = false")
	List<ImportEntity> findAllWithProduct();

	List<ImportEntity> findByImportDateBetweenAndIsDeletedFalse(LocalDate start, LocalDate end);

	@Query("SELECT SUM(i.quantity) FROM ImportEntity i WHERE i.product.id = :productId AND i.isDeleted = false")
	Integer getTotalImportedQuantity(@Param("productId") Integer productId);

	@Query("SELECT SUM(i.totalAmount) FROM ImportEntity i WHERE i.importDate = :date AND i.isDeleted = false")
	Double sumTotalAmountByDate(@Param("date") LocalDate date);

	@Query("SELECT SUM(i.totalAmount) FROM ImportEntity i WHERE i.importDate BETWEEN :from AND :to AND i.isDeleted = false")
	Double sumTotalAmountBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);

	@Query("SELECT i FROM ImportEntity i WHERE i.product.id = :productId AND i.isDeleted = false ORDER BY i.importDate DESC")
	List<ImportEntity> findLatestByProductId(@Param("productId") Integer productId);

}
