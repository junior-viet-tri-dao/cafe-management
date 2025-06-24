
package com.viettridao.cafe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viettridao.cafe.model.EmployeeEntity;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {
	Optional<EmployeeEntity> findByAccount_Username(String username);
}
