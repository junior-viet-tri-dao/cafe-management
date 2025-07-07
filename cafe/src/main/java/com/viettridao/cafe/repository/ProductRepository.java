package com.viettridao.cafe.repository;

/**
 * Repository cho thực thể ProductEntity.
 * Chịu trách nhiệm truy vấn dữ liệu liên quan đến sản phẩm (Product) từ cơ sở dữ liệu.
 */

import com.viettridao.cafe.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository cho thực thể ProductEntity.
 * Chịu trách nhiệm truy vấn dữ liệu liên quan đến sản phẩm (Product) từ cơ sở
 * dữ liệu.
 */
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {

    /**
     * Lấy danh sách tất cả các sản phẩm dựa trên trạng thái xóa.
     *
     * @param isDeleted Trạng thái xóa của sản phẩm (true: đã xóa, false: chưa xóa).
     * @return Danh sách các thực thể ProductEntity theo trạng thái xóa.
     */
    List<ProductEntity> findAllByIsDeleted(Boolean isDeleted);
}
