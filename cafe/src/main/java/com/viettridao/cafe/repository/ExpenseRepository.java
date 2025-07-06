package com.viettridao.cafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import com.viettridao.cafe.model.ExpenseEntity;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Integer> {

    List<ExpenseEntity> getAllByDeletedFalse();

    List<ExpenseEntity> findByExpenseDateBetweenAndDeletedFalse(LocalDate start, LocalDate end);

}
