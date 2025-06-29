package com.viettridao.cafe.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.ImportEntity;

@Repository
public interface ImportRepository extends JpaRepository<ImportEntity, Integer> {

    List<ImportEntity> findAllByDeletedFalse();

    List<ImportEntity> findByImportDateBetweenAndDeletedFalse(LocalDate start, LocalDate end);
}
