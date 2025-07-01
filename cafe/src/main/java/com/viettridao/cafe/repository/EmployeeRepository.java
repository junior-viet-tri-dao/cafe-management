package com.viettridao.cafe.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viettridao.cafe.model.EmployeeEntity;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {
	@Query("select e from EmployeeEntity e where e.isDeleted = false and lower(e.fullName) like lower(CONCAT('%', :keyword, '%')) ")
	Page<EmployeeEntity> getAllEmployeesBySearch(@Param("keyword") String keyword, Pageable pageable);

	@Query("select e from EmployeeEntity e where e.isDeleted = false")
	Page<EmployeeEntity> getAllEmployees(Pageable pageable);

	List<EmployeeEntity> findByIsDeletedFalse();

	@Query("""
			    SELECT e FROM EmployeeEntity e
			    WHERE e.account.username = :username AND e.isDeleted = false
			""")
	Optional<EmployeeEntity> findByAccountUsername(@Param("username") String username);

}
