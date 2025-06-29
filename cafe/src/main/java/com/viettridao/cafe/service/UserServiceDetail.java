package com.viettridao.cafe.service;

import com.viettridao.cafe.model.AccountEntity;
import com.viettridao.cafe.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceDetail implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountEntity account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản có username = " + username));
        return User.withUsername(account.getUsername())
                .password(account.getPassword())
                .authorities(account.getPermission() == null ? "USER" : account.getPermission().toUpperCase())
                .build();
    }
}
