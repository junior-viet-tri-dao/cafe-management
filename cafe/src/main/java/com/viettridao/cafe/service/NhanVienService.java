package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.employee.CreateEmployeeDTO;
import com.viettridao.cafe.dto.employee.EmployeeDTO;
import com.viettridao.cafe.dto.employee.UpdateEmployeeDTO;
import com.viettridao.cafe.model.NhanVien;
import java.util.List;

public interface NhanVienService {
    NhanVien getNhanVienById(int id);
    List<EmployeeDTO> getAllNhanVien();
    NhanVien createEmployee(CreateEmployeeDTO employeeDTO);
    void deleteEmployee(Integer id);
    UpdateEmployeeDTO getEmployeeById(Integer id);
    void updateEmployee(UpdateEmployeeDTO employeeDTO);
}
