package com.viettridao.cafe.service;

import com.viettridao.cafe.model.AccountEntity;
import com.viettridao.cafe.repository.AccountRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
public class UserServiceDetail implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountEntity account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        if (account.getEmployee() == null) {
            throw new RuntimeException("No Employee associated with account: " + username);
        }
        return account; // Assuming AccountEntity implements UserDetails
    }
}
