package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Integer> {
    List<ExpenseEntity> findAllByIsDeletedFalseOrderByExpenseDateDesc();

    List<ExpenseEntity> findAllByExpenseDateBetweenAndIsDeletedFalse(LocalDate from, LocalDate to);
}
