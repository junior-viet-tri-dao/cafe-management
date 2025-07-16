package com.viettridao.cafe.service;


import com.viettridao.cafe.dto.request.account.UpdateAccountRequest;
import com.viettridao.cafe.model.AccountEntity;

public interface AccountService {


    AccountEntity getAccountById(Integer id);
    void updateAccount(UpdateAccountRequest request);

    AccountEntity getAccountByUsername(String name);
}
