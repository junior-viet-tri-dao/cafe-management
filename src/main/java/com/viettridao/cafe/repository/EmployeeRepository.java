package com.viettridao.cafe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.dto.response.employee.SearchEmployeeResponse;
import com.viettridao.cafe.model.EmployeeEntity;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<EmployeeEntity> findByPhoneNumber(String phoneNumber);

    @Query(value = """
            SELECT e.employee_id AS employeeId,
                   e.full_name AS fullName,
                   p.position_name AS positionName,
                   p.salary AS salary
            FROM employees e
            JOIN positions p ON e.position_id = p.position_id
            WHERE LOWER(e.full_name) LIKE CONCAT('%', LOWER(:keyword), '%')
            """, nativeQuery = true)
    List<SearchEmployeeResponse> searchByName(@Param("keyword") String keyword);

}
