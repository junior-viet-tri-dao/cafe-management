package com.viettridao.cafe.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.dto.request.expenses.BudgetFilterRequest;
import com.viettridao.cafe.dto.request.expenses.ExpenseRequest;
import com.viettridao.cafe.dto.response.expenses.BudgetViewResponse;
import com.viettridao.cafe.mapper.EquipmentMapper;
import com.viettridao.cafe.mapper.ExpenseMapper;
import com.viettridao.cafe.mapper.IncomeMapper;
import com.viettridao.cafe.model.AccountEntity;
import com.viettridao.cafe.model.EquipmentEntity;
import com.viettridao.cafe.model.ExpenseEntity;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.repository.AccountRepository;
import com.viettridao.cafe.repository.EquipmentRepository;
import com.viettridao.cafe.repository.ExpenseRepository;
import com.viettridao.cafe.repository.InvoiceRepository;
import com.viettridao.cafe.service.BudgetService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

	private final ExpenseRepository expenseRepo;
	private final InvoiceRepository invoiceRepo;
	private final EquipmentRepository equipmentRepo;
	private final AccountRepository accountRepo;

	private final ExpenseMapper expenseMapper;
	private final IncomeMapper incomeMapper;
	private final EquipmentMapper equipmentMapper;

	@Override
	public Page<BudgetViewResponse> getBudgetView(BudgetFilterRequest request) {
		LocalDate from = request.getFromDate();
		LocalDate to = request.getToDate();

		List<InvoiceEntity> invoices = invoiceRepo.findByStatusAndCreatedAtBetween(InvoiceStatus.PAID,
				from.atStartOfDay(), to.plusDays(1).atStartOfDay());

		List<ExpenseEntity> expenses = expenseRepo.findExpensesBetweenDates(from, to);
		List<EquipmentEntity> equipmentList = equipmentRepo.findEquipmentsBetweenDates(from, to);

		List<BudgetViewResponse> incomeDtos = invoices.stream().map(incomeMapper::fromInvoice)
				.collect(Collectors.toList());

		List<BudgetViewResponse> expenseDtos = expenses.stream().map(expenseMapper::toDto).collect(Collectors.toList());

		List<BudgetViewResponse> equipmentDtos = equipmentList.stream().map(equipmentMapper::toBudgetDto)
				.collect(Collectors.toList());

		Map<LocalDate, BudgetViewResponse> merged = new HashMap<>();

		for (BudgetViewResponse income : incomeDtos) {
			merged.put(income.getDate(), new BudgetViewResponse(income.getDate(), income.getIncome(), 0.0));
		}

		expenseDtos.forEach(dto -> mergeExpense(merged, dto));

		equipmentDtos.forEach(dto -> mergeExpense(merged, dto));

		List<BudgetViewResponse> result = new ArrayList<>(merged.values());
		result.sort(Comparator.comparing(BudgetViewResponse::getDate).reversed());

		int start = request.getPage() * request.getSize();
		if (start >= result.size()) {
			return Page.empty(PageRequest.of(request.getPage(), request.getSize()));
		}
		int end = Math.min(start + request.getSize(), result.size());
		List<BudgetViewResponse> pageContent = result.subList(start, end);

		return new PageImpl<>(pageContent, PageRequest.of(request.getPage(), request.getSize()), result.size());
	}

	private void mergeExpense(Map<LocalDate, BudgetViewResponse> map, BudgetViewResponse dto) {
		map.merge(dto.getDate(), dto, (oldVal, newVal) -> {
			oldVal.setExpense(oldVal.getExpense() + newVal.getExpense());
			return oldVal;
		});
	}

	@Override
	public void addExpense(ExpenseRequest request, String username) {
		AccountEntity account = accountRepo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Tài khoản không tồn tại"));

		ExpenseEntity entity = expenseMapper.fromRequest(request);
		entity.setAccount(account);
		expenseRepo.save(entity);
	}
}
