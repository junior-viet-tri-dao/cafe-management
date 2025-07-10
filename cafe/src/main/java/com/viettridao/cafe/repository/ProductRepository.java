package com.viettridao.cafe.repository;

/**
 * Repository cho thực thể ProductEntity.
 * Chịu trách nhiệm truy vấn dữ liệu liên quan đến sản phẩm (Product) từ cơ sở dữ liệu.
 */

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.ProductEntity;

import java.util.List;

/**
 * Repository cho thực thể ProductEntity.
 * Chịu trách nhiệm truy vấn dữ liệu liên quan đến sản phẩm (Product) từ cơ sở
 * dữ liệu.
 */
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    @Query("select p from ProductEntity p where p.isDeleted = false and lower(p.productName) like lower(CONCAT('%', :keyword, '%')) ")
    // Lấy danh sách hàng hóa không bị xóa mềm và tìm kiếm theo từ khóa trong tên
    Page<ProductEntity> getAllProductBySearch(@Param("keyword") String keyword, Pageable pageable);

    List<ProductEntity> findAllByIsDeletedFalse();

    @Query("select p from ProductEntity p where p.isDeleted = false")
    // Lấy danh sách tất cả hàng hóa không bị xóa mềm
    Page<ProductEntity> getAllProducts(Pageable pageable);

    boolean existsByProductNameAndIsDeletedFalse(String productName);

    // Kiểm tra trùng tên, loại trừ chính nó (không tính sản phẩm đang cập nhật)
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM ProductEntity p WHERE p.isDeleted = false AND lower(p.productName) = lower(:productName) AND p.id <> :productId")
    boolean existsByProductNameAndIsDeletedFalseAndIdNot(@Param("productName") String productName,
            @Param("productId") Integer productId);
}
