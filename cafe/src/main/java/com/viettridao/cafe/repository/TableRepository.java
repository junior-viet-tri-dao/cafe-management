package com.viettridao.cafe.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.TableEntity;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Integer> {

    // Lấy tất cả bàn chưa xóa (không phân trang)
    List<TableEntity> findByIsDeletedFalse();

    // Lấy tất cả bàn chưa xóa (có phân trang) — khớp với TableServiceImpl
    Page<TableEntity> findByIsDeletedFalse(Pageable pageable);

    // Nếu sau này bạn muốn dùng tìm kiếm tên bàn
    Page<TableEntity> findByIsDeletedFalseAndTableNameContainingIgnoreCase(String keyword, Pageable pageable);
    
    TableEntity findByReservations_Invoice_Id(Integer invoiceId);

}

