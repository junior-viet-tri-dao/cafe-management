package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.request.employee.CreateEmployeeRequest;
import com.viettridao.cafe.dto.request.employee.UpdateEmployeeRequest;
import com.viettridao.cafe.dto.response.employee.EmployeePageResponse;
import com.viettridao.cafe.dto.response.employee.EmployeeResponse;
import com.viettridao.cafe.mapper.EmployeeMapper;
import com.viettridao.cafe.mapper.PositionMapper;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.service.EmployeeService;
import com.viettridao.cafe.service.PositionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest(EmployeeController.class)
@WithMockUser(username = "admin", roles = { "ADMIN" })
class EmployeeControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeService employeeService;
    @MockBean
    private EmployeeMapper employeeMapper;
    @MockBean
    private PositionService positionService;
    @MockBean
    private PositionMapper positionMapper;

    @Test
    @DisplayName("GET /employee trả về trang danh sách nhân viên")
    void testHome() throws Exception {
        EmployeePageResponse pageResponse = new EmployeePageResponse();
        pageResponse.setEmployees(Collections.emptyList());
        when(employeeService.getAllEmployees(any(), anyInt(), anyInt())).thenReturn(pageResponse);
        mockMvc.perform(MockMvcRequestBuilders.get("/employee"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/employees/employee"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("employees"));
    }

    @Test
    @DisplayName("GET /employee/create trả về form tạo nhân viên")
    void testShowFormCreate() throws Exception {
        when(positionService.getPositions()).thenReturn(Collections.emptyList());
        when(positionMapper.toListPositionResponse(any())).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/employee/create"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/employees/create_employee"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("positions"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("employee"));
    }

    @Test
    @DisplayName("POST /employee/delete/{id} thành công")
    void testDeleteEmployee_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/employee/delete/1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/employee"));
        verify(employeeService).deleteEmployee(1);
    }

    @Test
    @DisplayName("GET /employee/update/{id} thành công")
    void testShowFormUpdate_Success() throws Exception {
        EmployeeEntity entity = new EmployeeEntity();
        EmployeeResponse response = new EmployeeResponse();
        when(employeeService.getEmployeeById(anyInt())).thenReturn(entity);
        when(employeeMapper.toEmployeeResponse(any())).thenReturn(response);
        when(positionService.getPositions()).thenReturn(Collections.emptyList());
        when(positionMapper.toListPositionResponse(any())).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/employee/update/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/employees/update_employee"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("positions"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("employee"));
    }
}
