package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.budget.CreateExpenseRequest;
import com.viettridao.cafe.dto.request.budget.UpdateExpenseRequest;
import com.viettridao.cafe.dto.response.budget.ExpensePageResponse;
import com.viettridao.cafe.model.ExpenseEntity;
import com.viettridao.cafe.repository.ExpenseRepository;
import com.viettridao.cafe.service.impl.ExpenseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ExpenseServiceTest {
    @MockBean
    private ExpenseRepository expenseRepository;
    @MockBean
    private com.viettridao.cafe.mapper.ExpenseMapper expenseMapper;

    @Autowired
    private ExpenseServiceImpl expenseService;

    @Test
    @DisplayName("Tạo khoản chi thành công")
    void testCreateExpense_Success() {
        CreateExpenseRequest request = new CreateExpenseRequest();
        request.setAmount(100.0);
        request.setExpenseName("Chi mua văn phòng phẩm");
        request.setExpenseDate(java.time.LocalDate.now());
        ExpenseEntity entity = new ExpenseEntity();
        entity.setId(1);
        when(expenseRepository.save(any(ExpenseEntity.class))).thenReturn(entity);
        assertThatCode(() -> expenseService.createExpense(request)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Cập nhật khoản chi thành công")
    void testUpdateExpense_Success() {
        UpdateExpenseRequest request = new UpdateExpenseRequest();
        request.setId(1);
        request.setAmount(200.0);
        request.setExpenseName("Chi mua thiết bị");
        request.setExpenseDate(java.time.LocalDate.now());
        ExpenseEntity entity = new ExpenseEntity();
        entity.setId(1);
        entity.setIsDeleted(false);
        when(expenseRepository.findById(1)).thenReturn(Optional.of(entity));
        when(expenseRepository.save(any(ExpenseEntity.class))).thenReturn(entity);
        assertThatCode(() -> expenseService.updateExpense(request)).doesNotThrowAnyException();
        assertThat(entity.getIsDeleted()).isFalse();
    }

    @Test
    @DisplayName("Xóa khoản chi thành công")
    void testDeleteExpense_Success() {
        ExpenseEntity entity = new ExpenseEntity();
        entity.setId(1);
        entity.setIsDeleted(false);
        when(expenseRepository.findById(1)).thenReturn(Optional.of(entity));
        when(expenseRepository.save(any(ExpenseEntity.class))).thenReturn(entity);
        assertThatCode(() -> expenseService.deleteExpense(1)).doesNotThrowAnyException();
        assertThat(entity.getIsDeleted()).isTrue();
    }

    @Test
    @DisplayName("Lấy danh sách khoản chi")
    void testGetAllExpenses() {
        // Mock Page<ExpenseEntity> trả về Page rỗng lần 1, Page có 1 phần tử lần 2
        java.util.List<ExpenseEntity> emptyList = java.util.Collections.emptyList();
        org.springframework.data.domain.Page<ExpenseEntity> mockPage = new org.springframework.data.domain.PageImpl<>(
                emptyList);
        ExpenseEntity entity = new ExpenseEntity();
        entity.setId(1);
        entity.setIsDeleted(false);
        java.util.List<ExpenseEntity> oneList = java.util.Collections.singletonList(entity);
        org.springframework.data.domain.Page<ExpenseEntity> onePage = new org.springframework.data.domain.PageImpl<>(
                oneList);
        // Mock mapper
        when(expenseMapper.toExpenseResponse(any(ExpenseEntity.class)))
                .thenReturn(new com.viettridao.cafe.dto.response.budget.ExpenseResponse());
        // Luôn trả về page hợp lệ: lần đầu page rỗng, lần sau page có 1 phần tử
        when(expenseRepository.findAll(any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(mockPage)
                .thenReturn(onePage);
        ExpensePageResponse response = expenseService.getAllExpenses(null, 0, 5);
        assertThat(response).isNotNull();
        assertThat(response.getExpenses()).isEmpty();
        ExpensePageResponse response2 = expenseService.getAllExpenses(null, 0, 5);
        assertThat(response2).isNotNull();
        assertThat(response2.getExpenses()).hasSize(1);
    }

    @Test
    @DisplayName("Cập nhật khoản chi không tồn tại")
    void testUpdateExpense_NotFound() {
        UpdateExpenseRequest request = new UpdateExpenseRequest();
        request.setId(999);
        when(expenseRepository.findById(999)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> expenseService.updateExpense(request))
                .isInstanceOf(java.util.NoSuchElementException.class)
                .hasMessage("Không tìm thấy khoản chi");
    }

    @Test
    @DisplayName("Xóa khoản chi không tồn tại")
    void testDeleteExpense_NotFound() {
        when(expenseRepository.findById(999)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> expenseService.deleteExpense(999))
                .isInstanceOf(java.util.NoSuchElementException.class)
                .hasMessage("Không tìm thấy khoản chi");
    }
}
