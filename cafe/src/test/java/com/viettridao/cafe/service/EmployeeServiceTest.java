
package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.employee.CreateEmployeeRequest;
import com.viettridao.cafe.dto.request.employee.UpdateEmployeeRequest;
import com.viettridao.cafe.model.AccountEntity;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.PositionEntity;
import com.viettridao.cafe.repository.AccountRepository;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.service.impl.EmployeeServiceImpl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest

class EmployeeServiceTest {
    @MockBean
    private EmployeeRepository employeeRepository;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private PositionService positionService;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmployeeServiceImpl employeeService;

    @Test
    @DisplayName("Tạo nhân viên thành công")
    void testCreateEmployee_Success() {
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setFullName("Nguyen Van A");
        request.setPhoneNumber("0123456789");
        request.setAddress("Hanoi");
        request.setPositionId(1);
        request.setUsername("user1");
        request.setPassword("pass1");
        request.setImageUrl("img.png");

        PositionEntity position = new PositionEntity();
        position.setId(1);
        when(positionService.getPositionById(1)).thenReturn(position);
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(accountRepository.save(any(AccountEntity.class))).thenAnswer(inv -> inv.getArgument(0));
        when(employeeRepository.save(any(EmployeeEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        assertThatCode(() -> employeeService.createEmployee(request)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Xóa nhân viên không tồn tại")
    void testDeleteEmployee_NotFound() {
        when(employeeRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> employeeService.deleteEmployee(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Không tìm thấy nhân viên");
    }

    @Test
    @DisplayName("Cập nhật nhân viên không tồn tại")
    void testUpdateEmployee_NotFound() {
        UpdateEmployeeRequest request = new UpdateEmployeeRequest();
        request.setId(99);
        when(employeeRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> employeeService.updateEmployee(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Không tìm thấy nhân viên");
    }
}
