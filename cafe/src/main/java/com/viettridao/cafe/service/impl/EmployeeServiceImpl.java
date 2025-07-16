package com.viettridao.cafe.service.impl;


import com.viettridao.cafe.dto.request.account.UpdateAccountRequest;
import com.viettridao.cafe.dto.request.employee.CreateEmployeeRequest;
import com.viettridao.cafe.dto.request.employee.UpdateEmployeeRequest;
import com.viettridao.cafe.dto.response.employee.EmployeeResponse;
import com.viettridao.cafe.dto.response.employee.EmployeeResponsePage;
import com.viettridao.cafe.model.AccountEntity;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.PositionEntity;
import com.viettridao.cafe.repository.AccountRepository;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.repository.PositionRepository;
import com.viettridao.cafe.service.AccountService;
import com.viettridao.cafe.service.EmployeeService;
import com.viettridao.cafe.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.util.StringUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.viettridao.cafe.mapper.EmployeeMapper;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final AccountRepository accountRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeemapper;
    private final PositionService positionService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public EmployeeResponsePage getAllEmployees(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<EmployeeEntity> employeeEntity;


        // Kiểm tra điều  kiện nếu có tìm kiếm và không có tìm kiếm
        if(StringUtils.hasText(keyword)){
            employeeEntity = employeeRepository.getAllEmployeesBySearch(keyword, pageable);
        }
        else{
            employeeEntity = employeeRepository.getAllEmployees(pageable);
        }

        EmployeeResponsePage employeePageResponse = new EmployeeResponsePage();
        employeePageResponse.setPageNumber(employeeEntity.getNumber());
        employeePageResponse.setPageSize(employeeEntity.getSize());
        employeePageResponse.setTotalPages(employeeEntity.getTotalPages());
        employeePageResponse.setTotalElements(employeeEntity.getTotalElements());
        employeePageResponse.setEmployees(employeemapper.toListEmployeeResponse(employeeEntity.getContent()));

        return employeePageResponse;


    }

    // Tạo mới employee với các thông tin cơ bản

    @Override
    public EmployeeEntity createEmployee(CreateEmployeeRequest request) {
        // khởi tạo đối tượng employee cơ bản và có thể xoá mềm được
        EmployeeEntity employee = new EmployeeEntity();
        employee.setFullName(request.getFullName().trim());
        employee.setPhoneNumber(request.getPhoneNumber().trim());
        employee.setAddress(request.getAddress().trim());
        employee.setIsDeleted(false);

        // nếu có id chức vụ thì tạo kèm với chức vụ
        if(request.getPositionId() != null)
        {
            PositionEntity position = positionService.getPositionByID(request.getPositionId());
            employee.setPosition(position);
        }

        // nếu có thông tin tài khoản và password thì sẽ tạo mới tài khoản, link ảnh, Encode mật khẩu, Set quyền mặc định là "EMPLOYEE", Liên kết account với nhân viên
        if(StringUtils.hasText(request.getUsername()) && StringUtils.hasText(request.getPassword())){
            AccountEntity accountEntity = new AccountEntity();
            accountEntity.setUsername(request.getUsername().trim());
            accountEntity.setPassword( passwordEncoder.encode( request.getPassword().trim()));
            accountEntity.setImageUrl(request.getImageUrl().trim());
            accountEntity.setIsDeleted(false);
            accountEntity.setPermission("EMPLOYEE");
            accountRepository.save(accountEntity);
            employee.setAccount(accountEntity);
        }

        EmployeeEntity result = employeeRepository.save(employee);
        return result;
    }

    @Override
    public boolean deleteEmployee(Integer id) {
        // Xoá đi đối tượng employee bằng cách xoá mềm, phục vụ chức năng khôi phục hoặc lưu lịch sử
        EmployeeEntity employeeEntity = getEmployeeById(id);
        employeeEntity.setIsDeleted(true);
        try{
            employeeRepository.save(employeeEntity);
            return  true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public void updateEmployee(UpdateEmployeeRequest request) {
        // Cập nhật thông tin cơ bản của employee
        EmployeeEntity employeeEntity = getEmployeeById(request.getId());
        employeeEntity.setAddress(request.getAddress().trim());
        employeeEntity.setFullName(request.getFullName().trim());
        employeeEntity.setPhoneNumber(request.getPhoneNumber().trim());

        // xử Lý chức vụ
        if(request.getPositionId() != null){
            PositionEntity positionEntity = positionService.getPositionByID(request.getPositionId());
            employeeEntity.setPosition(positionEntity);
        }

        // xử lý thông tin đăng nhập
        if(StringUtils.hasText(request.getUsername()) && StringUtils.hasText(request.getPassword())){
            // INếu có thông tin đăng nhập, kiểm tra xem đã có user chưa?
            Optional<AccountEntity> accountCheck = accountRepository.findByUsername(request.getUsername().trim());

            if(accountCheck.isPresent()){
                AccountEntity account = accountCheck.get();

                if(account.getEmployee().getId() != employeeEntity.getId()){
                    throw new RuntimeException("Tên đăng nhập trùng với nhân viên khác");
                }
                account.setPassword(passwordEncoder.encode(request.getPassword().trim()));
                account.setImageUrl(request.getImageUrl().trim());

                accountRepository.save(account);

            }
            else{
                throw new RuntimeException("Không có tài khoản cần cập nhật, Vui lòng kiểm tra lại");
            }

        }
        employeeRepository.save(employeeEntity);

    }

    @Override
    public EmployeeEntity getEmployeeById(Integer id) {
        return employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm dược nhân viên có ID = " + id));
    }

    @Override
    public List<EmployeeEntity> findEmployeeByIsDeletedFalse() {
        return List.of();
    }
}