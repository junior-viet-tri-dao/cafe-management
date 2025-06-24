package com.viettridao.cafe.service.impl;

import org.springframework.stereotype.Service;

import com.viettridao.cafe.model.AccountEntity;
import com.viettridao.cafe.repository.AccountRepository;
import com.viettridao.cafe.service.AccountService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public AccountEntity findByUsername(String username) {
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản có username = " + username));
    }

    @Override
    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    @Override
    public AccountEntity save(AccountEntity account) {
        return accountRepository.save(account);
    }

}
