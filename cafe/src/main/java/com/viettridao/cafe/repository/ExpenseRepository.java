package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.ExpenseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Integer> {

    List<ExpenseEntity> getAllByIsDeletedFalse();

    List<ExpenseEntity> findByExpenseDateBetweenAndIsDeletedFalse(LocalDate start, LocalDate end);

    Page<ExpenseEntity> findAllByIsDeletedFalse(Pageable pageable);
}
