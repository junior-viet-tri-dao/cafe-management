package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.EmployeeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {
    @Query("select e from EmployeeEntity e where e.isDeleted = false and lower(e.fullName) like lower(CONCAT('%', :keyword, '%')) ")
    // Lấy danh sách nhân viên không bị xóa mềm và tìm kiếm theo từ khóa trong tên
    Page<EmployeeEntity> getAllEmployeesBySearch(@Param("keyword") String keyword, Pageable pageable);

    @Query("select e from EmployeeEntity e where e.isDeleted = false")
    // Lấy danh sách tất cả nhân viên không bị xóa mềm
    Page<EmployeeEntity> getAllEmployees(Pageable pageable);
}
