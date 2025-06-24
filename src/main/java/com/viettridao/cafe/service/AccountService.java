package com.viettridao.cafe.service;

import com.viettridao.cafe.model.AccountEntity;

public interface AccountService {
    AccountEntity findByUsername(String username);
}
