package com.viettridao.cafe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.ImportEntity;

@Repository
public interface ImportRepository extends JpaRepository<ImportEntity, Integer> {

    // Truy vấn đơn nhập có chứa từ khóa trong tên hàng hóa
    Page<ImportEntity> findByProduct_ProductNameContainingIgnoreCase(String keyword, Pageable pageable);

}
