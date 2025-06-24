package com.viettridao.cafe.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viettridao.cafe.controller.Request.EmployeeProfileRequest;
import com.viettridao.cafe.controller.response.EmployeeProfileResponse;
import com.viettridao.cafe.dto.EmployeeProfileDTO;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.service.EmployeeService;

/**
 * Lớp triển khai dịch vụ quản lý thông tin nhân viên.
 * Cung cấp các phương thức để lấy, cập nhật và chuyển đổi thông tin hồ sơ nhân viên.
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    // Repository để thao tác với dữ liệu nhân viên trong cơ sở dữ liệu
    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Lấy thông tin hồ sơ nhân viên dựa trên tên đăng nhập.
     * @param username Tên đăng nhập của nhân viên
     * @return EmployeeProfileResponse chứa thông tin hồ sơ nhân viên
     * @throws RuntimeException nếu không thể lấy thông tin hồ sơ
     */
    @Override
    public EmployeeProfileResponse getProfileByUsername(String username) {
        try {
            // Tìm nhân viên và chuyển đổi sang response
            return convertToResponse(findByUsername(username));
        } catch (Exception e) {
            throw new RuntimeException("Không thể lấy thông tin hồ sơ nhân viên: " + e.getMessage());
        }
    }

    /**
     * Cập nhật thông tin hồ sơ nhân viên dựa trên yêu cầu.
     * @param request Đối tượng chứa thông tin cần cập nhật
     * @throws RuntimeException nếu không tìm thấy nhân viên hoặc có lỗi khi cập nhật
     */
    @Override
    public void updateProfile(EmployeeProfileRequest request) {
        try {
            // Tìm nhân viên theo ID, ném ngoại lệ nếu không tìm thấy
            EmployeeEntity employee = employeeRepository.findById(request.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));

            // Cập nhật các thông tin cơ bản của nhân viên
            employee.setFullName(request.getFullName());
            employee.setAddress(request.getAddress());
            employee.setPhoneNumber(request.getPhoneNumber());

            // Cập nhật thông tin vị trí và lương nếu vị trí tồn tại
            if (employee.getPosition() != null) {
                employee.getPosition().setPositionName(request.getPosition());
                employee.getPosition().setSalary(request.getSalary());
            }

            // Cập nhật URL ảnh đại diện nếu được cung cấp và không rỗng
            if (request.getImageUrl() != null && !request.getImageUrl().isBlank()) {
                employee.getAccount().setImageUrl(request.getImageUrl());
            }

            // Lưu thông tin nhân viên đã cập nhật vào cơ sở dữ liệu
            employeeRepository.save(employee);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi cập nhật hồ sơ nhân viên: " + e.getMessage());
        }
    }

    /**
     * Tìm nhân viên dựa trên tên đăng nhập.
     * @param username Tên đăng nhập của tài khoản liên kết
     * @return EmployeeEntity nếu tìm thấy
     * @throws RuntimeException nếu không tìm thấy nhân viên hoặc có lỗi
     */
    @Override
    public EmployeeEntity findByUsername(String username) {
        try {
            // Tìm nhân viên theo username, ném ngoại lệ nếu không tìm thấy
            return employeeRepository.findByAccount_Username(username)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với username: " + username));
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tìm nhân viên: " + e.getMessage());
        }
    }

    /**
     * Chuyển đổi thông tin nhân viên sang đối tượng response.
     * @param employee Thực thể nhân viên
     * @return EmployeeProfileResponse chứa thông tin hồ sơ nhân viên
     * @throws RuntimeException nếu có lỗi khi chuyển đổi
     */
    @Override
    public EmployeeProfileResponse convertToResponse(EmployeeEntity employee) {
        try {
            // Tạo đối tượng response
            EmployeeProfileResponse res = new EmployeeProfileResponse();
            // Gán các thông tin cơ bản
            res.setId(employee.getId());
            res.setFullName(employee.getFullName());
            res.setAddress(employee.getAddress());
            res.setPhoneNumber(employee.getPhoneNumber());

            // Gán thông tin vị trí và lương nếu vị trí tồn tại
            if (employee.getPosition() != null) {
                res.setPosition(employee.getPosition().getPositionName());
                res.setSalary(employee.getPosition().getSalary());
            }

            // Gán thông tin tài khoản nếu tài khoản tồn tại
            if (employee.getAccount() != null) {
                res.setUsername(employee.getAccount().getUsername());
                res.setPassword(employee.getAccount().getPassword());
                res.setImageUrl(employee.getAccount().getImageUrl());
            }

            return res;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi chuyển đổi dữ liệu nhân viên sang response: " + e.getMessage());
        }
    }

    /**
     * Lấy thông tin hồ sơ nhân viên dưới dạng DTO dựa trên tên đăng nhập.
     * @param username Tên đăng nhập của nhân viên
     * @return EmployeeProfileDTO chứa thông tin hồ sơ nhân viên
     * @throws RuntimeException nếu có lỗi khi lấy DTO
     */
    @Override
    public EmployeeProfileDTO getProfileDTO(String username) {
        try {
            // Tìm nhân viên theo username
            EmployeeEntity employee = findByUsername(username);

            // Tạo và trả về DTO với các thông tin từ nhân viên
            return new EmployeeProfileDTO(
                    employee.getId(),
                    employee.getFullName(),
                    employee.getPosition() != null ? employee.getPosition().getPositionName() : null,
                    employee.getAddress(),
                    employee.getPhoneNumber(),
                    employee.getPosition() != null ? employee.getPosition().getSalary() : null,
                    employee.getAccount() != null ? employee.getAccount().getUsername() : null,
                    employee.getAccount() != null ? employee.getAccount().getPassword() : null,
                    employee.getAccount() != null ? employee.getAccount().getImageUrl() : null
            );
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy hồ sơ DTO nhân viên: " + e.getMessage());
        }
    }
}