package com.viettridao.cafe.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.viettridao.cafe.model.AccountEntity;
import com.viettridao.cafe.repository.AccountRepository;

@Service
@RequiredArgsConstructor
@Getter
public class UserServiceDetail implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountEntity account = accountRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        if (account.getEmployee() == null) {
            throw new RuntimeException("No Employee associated with account: " + username);
        }
        return account;
    }
}
