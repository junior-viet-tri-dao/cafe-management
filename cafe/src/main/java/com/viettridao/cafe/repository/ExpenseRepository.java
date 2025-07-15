package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Integer> {
    @Query("SELECT e FROM ExpenseEntity e WHERE e.expenseDate BETWEEN :from AND :to AND e.isDeleted = false")
    List<ExpenseEntity> findExpensesBetweenDates(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query("SELECT SUM(e.amount) FROM ExpenseEntity e WHERE e.expenseDate = :date AND e.isDeleted = false")
    Double sumAmountByDate(@Param("date") LocalDate date);

    @Query("SELECT SUM(e.amount) FROM ExpenseEntity e WHERE e.expenseDate BETWEEN :from AND :to AND e.isDeleted = false")
    Double sumAmountBetween(@Param("from") LocalDate from, @Param("to") LocalDate to);

    List<ExpenseEntity> findByExpenseDateBetweenAndIsDeletedFalse(LocalDate start, LocalDate end);

}
