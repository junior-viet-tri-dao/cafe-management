package com.viettridao.cafe.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.viettridao.cafe.repository.AccountRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Getter
public class UserServiceDetail implements UserDetailsService {
	private final AccountRepository accountRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return accountRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản có username = " + username));
	}
}
