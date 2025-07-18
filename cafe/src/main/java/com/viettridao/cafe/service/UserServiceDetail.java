package com.viettridao.cafe.service;

import com.viettridao.cafe.repository.AccountRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserServiceDetail
 *
 * Version 1.0
 *
 * Date: 18-07-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE         AUTHOR      DESCRIPTION
 * -------------------------------------------------------
 * 18-07-2025   mirodoan    Create
 *
 * Service chi tiết người dùng (UserServiceDetail).
 * Chịu trách nhiệm xử lý logic nghiệp vụ liên quan đến xác thực và tải thông tin người dùng.
 */
@Service
@RequiredArgsConstructor
@Getter
public class UserServiceDetail implements UserDetailsService {

    // Repository cho thực thể AccountEntity
    private final AccountRepository accountRepository;

    /**
     * Tải thông tin người dùng dựa trên tên đăng nhập.
     *
     * @param username Tên đăng nhập của người dùng.
     * @return Đối tượng UserDetails chứa thông tin người dùng.
     * @throws UsernameNotFoundException Nếu không tìm thấy người dùng với tên đăng nhập đã cung cấp.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản có username = " + username));
    }
}