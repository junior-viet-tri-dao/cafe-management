package com.vn.repository;

import com.vn.model.Role;
import com.vn.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {

    boolean existsByUsername(String username);
    Users findByUsername(String username);
       
    @Query("SELECT u FROM Users u WHERE (:keyword IS NULL OR u.hoTen LIKE %:keyword%)")
    Page<Users> searchEmployee(@Param("keyword") String keyword, Pageable pageable);
       
    // boolean existsByEmail(String email);
    // boolean existsByPhone(String phone);
    // boolean existsByIdentityCard(String identityCard);

    // @Query("SELECT u FROM Users u WHERE u.role = 'Customer' AND u.hoTen LIKE CONCAT('%', :keyword, '%')")
    // Page<Users> searchCustomer(@Param("keyword") String keyword, Pageable pageable);

    

    // Users findByUserIdAndRole(Integer id, Role role);

    // @Query("SELECT u FROM Users u WHERE u.role = 'Customer'")
    // List<Users> findAllCustomers();

}
