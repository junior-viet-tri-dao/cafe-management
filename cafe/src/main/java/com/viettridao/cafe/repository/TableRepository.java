package com.viettridao.cafe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.TableEntity;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Integer> {
	List<TableEntity> findByIsDeletedFalse();
}
