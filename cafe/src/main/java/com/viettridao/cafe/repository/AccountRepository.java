package com.viettridao.cafe.repository;

import com.viettridao.cafe.dto.response.account.AccountResponse;
import com.viettridao.cafe.model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
    Optional<AccountEntity> findByUsername(String username);

    @Query("""
    SELECT new com.viettridao.cafe.dto.response.account.AccountResponse(
    tk.id, nv.fullName, nv.phoneNumber, nv.address, tk.imageUrl,
     cv.id, cv.positionName, cv.salary, tk.username, tk.password)
    FROM AccountEntity tk
    LEFT JOIN tk.employee nv
    LEFT JOIN nv.position cv
    WHERE tk.username = :username
    """)
    AccountResponse getAccountByUsername(@Param("username") String username);
    
    
}
