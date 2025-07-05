package com.viettridao.cafe.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.TableEntity;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Integer> {

	List<TableEntity> findByIsDeletedFalse();

	Page<TableEntity> findByIsDeletedFalse(Pageable pageable);

	Page<TableEntity> findByIsDeletedFalseAndTableNameContainingIgnoreCase(String keyword, Pageable pageable);

	TableEntity findByReservations_Invoice_Id(Integer invoiceId);

}
