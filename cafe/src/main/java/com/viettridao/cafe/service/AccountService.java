package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.account.UpdateAccountRequest;
import com.viettridao.cafe.model.AccountEntity;

public interface AccountService {
    void updateAccount(UpdateAccountRequest request);

    AccountEntity getAccountById(Integer id);

    AccountEntity getAccountByUsername(String username);
}
