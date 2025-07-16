package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.ImportEntity;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.model.PromotionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ImportRepository extends JpaRepository<ImportEntity, Integer> {

    @Query("SELECT SUM(i.quantity) FROM ImportEntity i WHERE i.product.id = :productId AND i.isDeleted = false")
    Integer getTotalImportedQuantity(@Param("productId") Integer productId);

    @Query("SELECT SUM(i.totalAmount) FROM ImportEntity i WHERE i.importDate = :date AND i.isDeleted = false")
    Double sumTotalAmountByDate(@Param("date") LocalDate date);

    @Query("SELECT SUM(i.totalAmount) FROM ImportEntity i WHERE i.importDate BETWEEN :from AND :to AND i.isDeleted = false")
    Double sumTotalAmountBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query("Select p from ImportEntity p where p.isDeleted = false and (p.totalAmount) = :keyword")
    Page<ImportEntity> getAllImportPageSearch(@Param("keyword") String keyword, Pageable pageable);


    @Query("Select p from ImportEntity p where p.isDeleted = false ")
    Page<ImportEntity> getAllImportPage(Pageable pageable);

    @Query("Select p from ImportEntity p where p.isDeleted = false and product.id = :productId")
    List<ImportEntity> getAllProductImportByIdProduct(Integer productId);

    ImportEntity[] findByImportDateBetweenAndIsDeletedFalse(LocalDate start, LocalDate end);
}
