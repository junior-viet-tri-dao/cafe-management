package com.viettridao.cafe.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.viettridao.cafe.dto.request.account.UpdateAccountRequest;
import com.viettridao.cafe.model.AccountEntity;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.repository.AccountRepository;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.service.AccountService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
	private final AccountRepository accountRepository;
	private final EmployeeRepository employeeRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	@Override
	public void updateAccount(UpdateAccountRequest request) {
		AccountEntity account = getAccountById(request.getId());

		if (StringUtils.hasText(request.getAddress()) || StringUtils.hasText(request.getFullName())
				|| StringUtils.hasText(request.getPhoneNumber())) {

			EmployeeEntity employee = account.getEmployee();

			if (employee == null) {
				employee = new EmployeeEntity();
				employee.setAccount(account);
			}

			employee.setFullName(request.getFullName());
			employee.setPhoneNumber(request.getPhoneNumber());
			employee.setAddress(request.getAddress());
			employee.setAccount(account);
			employeeRepository.save(employee);

			account.setEmployee(employee);
		}

		if (StringUtils.hasText(request.getPassword())) {
			account.setPassword(passwordEncoder.encode(request.getPassword()));
		}

		accountRepository.save(account);
	}

	@Override
	public AccountEntity getAccountById(Integer id) {
		return accountRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản có id=" + id));
	}

	@Override
	public AccountEntity getAccountByUsername(String username) {
		return accountRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản có username=" + username));
	}
}
