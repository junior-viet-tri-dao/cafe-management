package com.viettridao.cafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.ExportEntity;

@Repository
public interface ExportRepository extends JpaRepository<ExportEntity, Integer> {

}
