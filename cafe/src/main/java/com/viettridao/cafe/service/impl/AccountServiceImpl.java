package com.viettridao.cafe.service.impl; // Khai báo gói cho lớp triển khai dịch vụ tài khoản

import com.viettridao.cafe.dto.request.account.UpdateAccountRequest; // Nhập lớp UpdateAccountRequest từ gói DTO
import com.viettridao.cafe.model.AccountEntity; // Nhập lớp AccountEntity từ gói model
import com.viettridao.cafe.model.EmployeeEntity; // Nhập lớp EmployeeEntity từ gói model
import com.viettridao.cafe.model.PositionEntity; // Nhập lớp PositionEntity từ gói model (hiện không được sử dụng trực tiếp nhưng có thể liên quan)
import com.viettridao.cafe.repository.AccountRepository; // Nhập giao diện AccountRepository từ gói repository
import com.viettridao.cafe.repository.EmployeeRepository; // Nhập giao diện EmployeeRepository từ gói repository
import com.viettridao.cafe.repository.PositionRepository; // Nhập giao diện PositionRepository từ gói repository (hiện không được sử dụng trực tiếp nhưng có thể liên quan)
import com.viettridao.cafe.service.AccountService; // Nhập giao diện AccountService từ gói service
import lombok.RequiredArgsConstructor; // Nhập chú thích RequiredArgsConstructor của Lombok để tạo constructor
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service; // Nhập chú thích Service của Spring để đánh dấu lớp là một dịch vụ
import org.springframework.transaction.annotation.Transactional; // Nhập chú thích Transactional của Spring để quản lý giao dịch
import org.springframework.util.StringUtils;

/**
 * AccountServiceImpl
 *
 * Lớp này triển khai giao diện AccountService và cung cấp logic nghiệp vụ
 * cho các hoạt động liên quan đến tài khoản người dùng.
 *
 * Phiên bản 1.0
 *
 * Ngày: 2025-07-23
 *
 * Bản quyền (c) 2025 VietTriDao. Đã đăng ký bản quyền.
 *
 * Nhật ký sửa đổi:
 * NGÀY                 TÁC GIẢ          MÔ TẢ
 * -----------------------------------------------------------------------
 * 2025-07-23           Hoa Mãn Lâu     Tạo và định dạng ban đầu.
 */
@Service // Đánh dấu lớp này là một Service của Spring
@RequiredArgsConstructor // Tự động tạo constructor với các trường final
public class AccountServiceImpl implements AccountService { // Triển khai giao diện AccountService

    /**
     * Tiêm phụ thuộc AccountRepository để truy cập dữ liệu tài khoản.
     */
    private final AccountRepository accountRepository; // Khai báo một trường final cho AccountRepository

    /**
     * Tiêm phụ thuộc EmployeeRepository để truy cập dữ liệu nhân viên.
     */
    private final EmployeeRepository employeeRepository; // Khai báo một trường final cho EmployeeRepository

    private  final PasswordEncoder passwordEncoder;

    /**
     * Lấy thông tin tài khoản theo ID.
     *
     * @param id ID của tài khoản cần lấy.
     * @return Đối tượng AccountEntity tương ứng với ID đã cho.
     * @throws RuntimeException Nếu không tìm thấy tài khoản với ID đã cho.
     */
    @Override // Ghi đè phương thức từ giao diện AccountService
    public AccountEntity getAccountById(Integer id) { // Định nghĩa phương thức để lấy tài khoản theo ID
        // Tìm tài khoản theo ID; nếu không tìm thấy, ném ra RuntimeException
        return accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy user có id=" + id));
    }

    /**
     * Cập nhật thông tin tài khoản và thông tin nhân viên liên quan.
     * Phương thức này được đánh dấu là @Transactional để đảm bảo tính toàn vẹn dữ liệu.
     *
     * @param request Đối tượng UpdateAccountRequest chứa thông tin cập nhật cho tài khoản.
     */
//    @Transactional // Đảm bảo phương thức này chạy trong một giao dịch cơ sở dữ liệu
//    @Override // Ghi đè phương thức từ giao diện AccountService
//    public void updateAccount(UpdateAccountRequest request) { // Định nghĩa phương thức để cập nhật tài khoản
//        // Lấy AccountEntity hiện có bằng ID từ request
//        AccountEntity accountEntity = getAccountById(request.getId());
//        // Lấy EmployeeEntity liên quan từ AccountEntity
//        EmployeeEntity employeeEntity = accountEntity.getEmployee();
//        // Dòng này được comment out, có thể liên quan đến PositionEntity
//        // PositionEntity positionEntity = employeeEntity.getPosition();
//
//        // Cập nhật thông tin địa chỉ của nhân viên từ request
//        employeeEntity.setAddress(request.getEmployee().getAddress());
//        // Cập nhật thông tin họ và tên của nhân viên từ request
//        employeeEntity.setFullName(request.getEmployee().getFullName());
//        // Cập nhật thông tin số điện thoại của nhân viên từ request
//        employeeEntity.setPhoneNumber(request.getEmployee().getPhoneNumber());
//
//        // Đặt lại EmployeeEntity đã cập nhật vào AccountEntity (có thể không cần thiết nếu employeeEntity là tham chiếu)
//        accountEntity.setEmployee(employeeEntity);
//        // Lưu EmployeeEntity đã cập nhật vào cơ sở dữ liệu
//        employeeRepository.save(employeeEntity);
//        // Lưu AccountEntity đã cập nhật vào cơ sở dữ liệu
//        accountRepository.save(accountEntity);
//    }

    @Transactional
    @Override
    public void updateAccount(UpdateAccountRequest request) {
        AccountEntity account = getAccountById(request.getId());

        if (StringUtils.hasText(request.getEmployee().getAddress()) ||
                StringUtils.hasText(request.getEmployee().getFullName()) ||
                StringUtils.hasText(request.getEmployee().getPhoneNumber())) {

            EmployeeEntity employee = account.getEmployee();

            if (employee == null) {
                employee = new EmployeeEntity();
                employee.setAccount(account);
            }

            // Cập nhật thông tin nhân viên liên kết với tài khoản
            employee.setFullName(request.getEmployee().getFullName());
            employee.setPhoneNumber(request.getEmployee().getPhoneNumber());
            employee.setAddress(request.getEmployee().getAddress());
            employee.setAccount(account);
            employeeRepository.save(employee);
           // account.setImageUrl(request.getImageUrl());
            account.setEmployee(employee);
        }

//        if (StringUtils.hasText(request.getPassword())) {
//            // Mã hóa và cập nhật mật khẩu mới
//            account.setPassword(passwordEncoder.encode(request.getEmployee().getPassword()));
//        }


        accountRepository.save(account);
    }

    /**
     * Lấy thông tin tài khoản theo tên người dùng.
     *
     * @param name Tên người dùng của tài khoản cần lấy.
     * @return Đối tượng AccountEntity tương ứng với tên người dùng đã cho.
     * @throws RuntimeException Nếu không tìm thấy tài khoản với tên người dùng đã cho.
     */
    @Override // Ghi đè phương thức từ giao diện AccountService
    public AccountEntity getAccountByUsername(String name) { // Định nghĩa phương thức để lấy tài khoản theo tên người dùng
        // Tìm tài khoản theo tên người dùng; nếu không tìm thấy, ném ra RuntimeException
        return accountRepository.findByUsername(name).orElseThrow(() -> new RuntimeException("Không tìm thấy account với username = " + name));
    }
}
