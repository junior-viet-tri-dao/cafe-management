package com.viettridao.cafe.repository;

import com.viettridao.cafe.model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
    // Tìm tài khoản theo tên đăng nhập
    Optional<AccountEntity> findByUsername(String username);
}
