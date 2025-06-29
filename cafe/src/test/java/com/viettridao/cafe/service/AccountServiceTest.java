package com.viettridao.cafe.service;

// import com.viettridao.cafe.dto.request.account.CreateAccountRequest;
import com.viettridao.cafe.dto.request.account.UpdateAccountRequest;
import com.viettridao.cafe.model.AccountEntity;
import com.viettridao.cafe.repository.AccountRepository;
import com.viettridao.cafe.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Không có CreateAccountRequest và createAccount trong service, bỏ qua test tạo
    // tài khoản

    @Test
    @DisplayName("Cập nhật tài khoản không tồn tại")
    void testUpdateAccount_NotFound() {
        UpdateAccountRequest request = new UpdateAccountRequest();
        request.setId(99);
        when(accountRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> accountService.updateAccount(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Không tìm thấy tài khoản");
    }

    // Không có deleteAccount trong service, bỏ qua test xóa tài khoản
}
