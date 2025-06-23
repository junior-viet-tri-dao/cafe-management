package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.account.AccountDTO;

public interface AccountService {
    AccountDTO getAccount(String username);
    void updateAccount(AccountDTO account);
}
