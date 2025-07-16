package com.viettridao.cafe.service.impl;


import com.viettridao.cafe.dto.request.account.UpdateAccountRequest;
import com.viettridao.cafe.model.AccountEntity;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.PositionEntity;
import com.viettridao.cafe.repository.AccountRepository;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.repository.PositionRepository;
import com.viettridao.cafe.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final EmployeeRepository employeeRepository;
//    private final PositionRepository positionRepository;

//
////    @Override public AccountEntity findById(Integer id) {
////        return null;
//////        return accountRepository.findById(id);
////    }
//
//
//
//
//
//    @Override
    public AccountEntity getAccountById(Integer id) {
        return accountRepository.findById(id).orElseThrow(()-> new RuntimeException("Không tìm thấy user có id=" + id));
    }


    @Transactional
    @Override
    public void updateAccount(UpdateAccountRequest request) {
        AccountEntity accountEntity = getAccountById(request.getId());
        EmployeeEntity employeeEntity = accountEntity.getEmployee();
//        PositionEntity positionEntity = employeeEntity.getPosition();
        employeeEntity.setAddress(request.getEmployee().getAddress());
        employeeEntity.setFullName(request.getEmployee().getFullName());
        employeeEntity.setPhoneNumber(request.getEmployee().getPhoneNumber());


        accountEntity.setEmployee(employeeEntity);
        employeeRepository.save(employeeEntity);
//        positionRepository.save(positionEntity);
        accountRepository.save(accountEntity);
    }

    @Override
    public AccountEntity getAccountByUsername(String name) {
        return accountRepository.findByUsername(name).orElseThrow(()-> new RuntimeException("Không tìm thấy account với username = " + name));
    }

}