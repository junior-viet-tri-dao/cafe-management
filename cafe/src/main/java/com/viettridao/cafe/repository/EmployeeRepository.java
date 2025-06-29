package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {
    Page<EmployeeEntity> findAllByIsDeletedFalseAndFullNameContainingIgnoreCase(String keyword, Pageable pageable);

    Page<EmployeeEntity> findAllByIsDeletedFalse(Pageable pageable);
}
