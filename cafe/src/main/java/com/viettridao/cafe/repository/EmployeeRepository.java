package com.viettridao.cafe.repository;

import com.viettridao.cafe.dto.response.employee.EmployeeResponsePage;
import com.viettridao.cafe.model.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {
    @Query("SELECT SUM(e.position.salary) FROM EmployeeEntity e WHERE e.isDeleted = false AND e.position IS NOT NULL")
    Double sumAllSalaries();
}
