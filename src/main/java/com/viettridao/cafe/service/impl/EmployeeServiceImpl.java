package com.viettridao.cafe.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.viettridao.cafe.dto.request.employee.AddEmployeeRequest;
import com.viettridao.cafe.dto.response.employee.SearchEmployeeResponse;
import com.viettridao.cafe.model.AccountEntity;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.PositionEntity;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.repository.PositionRepository;
import com.viettridao.cafe.service.AccountService;
import com.viettridao.cafe.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final PositionRepository positionRepository;

    @Override
    public Page<SearchEmployeeResponse> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable)
                .map(employee -> new SearchEmployeeResponse(
                        employee.getId(),
                        employee.getFullName(),
                        employee.getPosition().getPositionName(),
                        employee.getPosition().getSalary()));
    }

    @Override
    public EmployeeEntity getEmployeeById(Integer id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    }

    @Override
    public void createEmployee(AddEmployeeRequest request, MultipartFile avatar) {
        // 1. Kiểm tra username đã tồn tại chưa
        if (accountService.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }

        // 2. Validate và kiểm tra số điện thoại
        validatePhoneNumber(request.getPhoneNumber());
        if (employeeRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new RuntimeException("Số điện thoại đã tồn tại trong hệ thống");
        }

        // 2. Tạo Account trước
        AccountEntity account = new AccountEntity();
        account.setUsername(request.getUsername());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setPermission("STAFF"); // Mặc định là STAFF
        account.setIsDeleted(false);

        // Upload avatar nếu có
        if (avatar != null && !avatar.isEmpty()) {
            String imageUrl = uploadAvatar(avatar);
            account.setImageUrl(imageUrl);
        }

        // Save account
        AccountEntity savedAccount = accountService.save(account);

        PositionEntity position = positionRepository.findById(request.getPositionId())
                .orElseThrow(() -> new RuntimeException("Chức vụ không tồn tại"));
        // 3. Tạo Employee và liên kết với Account
        EmployeeEntity employee = new EmployeeEntity();
        employee.setFullName(request.getFullName());
        employee.setPhoneNumber(request.getPhoneNumber());
        employee.setAddress(request.getAddress());
        employee.setAccount(savedAccount); // Liên kết với Account

        if (position == null) {
            throw new RuntimeException("Không tìm thấy chức vụ");
        }

        employee.setPosition(position);
        employee.setIsDeleted(false);

        // Save employee
        employeeRepository.save(employee);
    }

    private String uploadAvatar(MultipartFile file) {
        try {
            // Logic upload file (có thể lưu local hoặc cloud)
            String uploadDir = "uploads/avatars/";
            String fileName = System.currentTimeMillis() + "_" +
                    file.getOriginalFilename();
            String filePath = uploadDir + fileName;

            // Tạo thư mục nếu chưa có
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Lưu file
            Files.copy(file.getInputStream(), Paths.get(filePath),
                    StandardCopyOption.REPLACE_EXISTING);

            return "/" + filePath; // Return relative path

        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi upload avatar: " + e.getMessage());
        }
    }

    private void validatePhoneNumber(String phoneNumber) {
        // Kiểm tra null hoặc empty
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new RuntimeException("Số điện thoại không được để trống");
        }

        // Loại bỏ khoảng trắng và ký tự đặc biệt
        String cleanPhone = phoneNumber.replaceAll("[\\s\\-\\(\\)]", "");

        // Validate định dạng số điện thoại Việt Nam
        if (!cleanPhone.matches("^(\\+84|84|0)(3[2-9]|5[2689]|7[06-9]|8[1-689]|9[0-46-9])\\d{7}$")) {
            throw new RuntimeException("Số điện thoại không đúng định dạng Việt Nam");
        }
    }

    @Override
    public List<SearchEmployeeResponse> searchByName(String keyword) {
        return employeeRepository.searchByName(keyword);
    }

    @Override
    public List<EmployeeEntity> getAllEmployeeDescSalary() {
        return employeeRepository.findAllEmployeeDescSalary();
    }

}
