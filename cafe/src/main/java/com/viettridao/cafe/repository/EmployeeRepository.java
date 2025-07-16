package com.viettridao.cafe.repository;

import com.viettridao.cafe.dto.response.employee.EmployeeResponsePage;
import com.viettridao.cafe.model.EmployeeEntity;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {
    @Query("SELECT SUM(e.position.salary) FROM EmployeeEntity e WHERE e.isDeleted = false AND e.position IS NOT NULL")
    Double sumAllSalaries();

    @Query("select e from EmployeeEntity e where e.isDeleted = false and lower(e.fullName) like lower(CONCAT('%', :keyword, '%')) ")
    Page<EmployeeEntity> getAllEmployeesBySearch(@Param("keyword") String keyword, Pageable pageable);

    @Query("select e from EmployeeEntity e where e.isDeleted = false")
    Page<EmployeeEntity> getAllEmployees(Pageable pageable);

    List<EmployeeEntity> findEmployeeByIsDeletedFalse();
}
