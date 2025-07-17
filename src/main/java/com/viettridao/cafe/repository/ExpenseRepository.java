package com.viettridao.cafe.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.ExpenseEntity;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Integer> {
    // Additional query methods can be defined here if needed
    @Query("SELECT e FROM ExpenseEntity e WHERE e.expenseDate BETWEEN :start AND :end AND e.isDeleted = false")
    List<ExpenseEntity> findAllByExpenseDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
