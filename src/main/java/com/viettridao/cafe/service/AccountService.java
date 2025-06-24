package com.viettridao.cafe.service;

import com.viettridao.cafe.model.AccountEntity;

public interface AccountService {
    AccountEntity findByUsername(String username);

    boolean existsByUsername(String username);

    AccountEntity save(AccountEntity account);
}
