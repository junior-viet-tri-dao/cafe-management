package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Integer> {
    @Query("SELECT SUM(e.amount) FROM ExpenseEntity e WHERE e.expenseDate = :date AND e.isDeleted = false")
    Double sumAmountByDate(@Param("date") LocalDate date);


    Iterable<? extends ExpenseEntity> findByExpenseDateBetweenAndIsDeletedFalse(LocalDate start, LocalDate end);
}
