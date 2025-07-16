package com.viettridao.cafe.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.viettridao.cafe.common.ReportType;
import com.viettridao.cafe.dto.response.employee.EmployeeDailySalaryResponse;
import com.viettridao.cafe.dto.response.reportstatistics.ReportItemResponse;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.PositionEntity;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.repository.ExpenseRepository;
import com.viettridao.cafe.repository.ExportRepository;
import com.viettridao.cafe.repository.ImportRepository;
import com.viettridao.cafe.repository.InvoiceRepository;
import com.viettridao.cafe.service.ReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

	private final InvoiceRepository invoiceRepository;
	private final ImportRepository importRepository;
	private final ExportRepository exportRepository;
	private final ExpenseRepository expenseRepository;
	private final EmployeeRepository employeeRepository;

	@Override
	public List<ReportItemResponse> getReport(LocalDate fromDate, LocalDate toDate) {
		List<ReportItemResponse> reports = new ArrayList<>();

		double totalMonthlySalary = employeeRepository.sumAllSalaries() != null ? employeeRepository.sumAllSalaries()
				: 0.0;

		for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
			Double revenue = invoiceRepository.sumTotalAmountByDate(date);
			Double importCost = importRepository.sumTotalAmountByDate(date);
			Double exportCost = exportRepository.sumTotalExportAmountByDate(date);
			Double otherExpenses = expenseRepository.sumAmountByDate(date);

			double totalRevenue = revenue != null ? revenue : 0.0;
			double totalExpense = 0.0;

			totalExpense += importCost != null ? importCost : 0.0;
			totalExpense += exportCost != null ? exportCost : 0.0;
			totalExpense += otherExpenses != null ? otherExpenses : 0.0;

			int daysInMonth = date.lengthOfMonth();
			double dailySalary = totalMonthlySalary / daysInMonth;

			totalExpense += dailySalary;

			reports.add(new ReportItemResponse(date, totalRevenue, totalExpense));
		}

		return reports;
	}

	@Override
	public List<EmployeeDailySalaryResponse> getEmployeeDailySalaries(LocalDate from, LocalDate to) {
		List<EmployeeDailySalaryResponse> result = new ArrayList<>();
		List<EmployeeEntity> employees = employeeRepository.findAll();

		for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
			int daysInMonth = date.lengthOfMonth();
			for (EmployeeEntity e : employees) {
				PositionEntity position = e.getPosition();
				double monthlySalary = (position != null && position.getSalary() != null) ? position.getSalary() : 0.0;
				double dailySalary = monthlySalary / daysInMonth;

				result.add(new EmployeeDailySalaryResponse(e.getFullName(), date, dailySalary));
			}
		}

		return result;
	}

	@Override
	public List<ReportItemResponse> getReport(LocalDate fromDate, LocalDate toDate, ReportType type) {
		List<ReportItemResponse> result = new ArrayList<>();

		double totalMonthlySalary = employeeRepository.sumAllSalaries() != null ? employeeRepository.sumAllSalaries()
				: 0.0;

		for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
			double revenue = 0.0;
			double expense = 0.0;

			if (type == ReportType.ALL || type == ReportType.SALE) {
				Double r = invoiceRepository.sumTotalAmountByDate(date);
				revenue = r != null ? r : 0.0;
			}

			switch (type) {
			case ALL -> {
				Double i = importRepository.sumTotalAmountByDate(date);
				Double e = exportRepository.sumTotalExportAmountByDate(date);
				Double o = expenseRepository.sumAmountByDate(date);

				expense += i != null ? i : 0.0;
				expense += e != null ? e : 0.0;
				expense += o != null ? o : 0.0;

				int daysInMonth = date.lengthOfMonth();
				expense += totalMonthlySalary / daysInMonth;
			}
			case IMPORT -> {
				Double i = importRepository.sumTotalAmountByDate(date);
				expense += i != null ? i : 0.0;
			}
			case EXPORT -> {
				Double e = exportRepository.sumTotalExportAmountByDate(date);
				expense += e != null ? e : 0.0;
			}
			case IMPORT_EXPORT -> {
				Double i = importRepository.sumTotalAmountByDate(date);
				Double e = exportRepository.sumTotalExportAmountByDate(date);
				expense += (i != null ? i : 0.0) + (e != null ? e : 0.0);
			}
			case OTHER_EXPENSE -> {
				Double o = expenseRepository.sumAmountByDate(date);
				expense += o != null ? o : 0.0;
			}
			case SALARY -> {
				int daysInMonth = date.lengthOfMonth();
				expense += totalMonthlySalary / daysInMonth;
			}
			case EMPLOYEE_INFO -> {
				expense += 0.0;
			}
			}

			result.add(new ReportItemResponse(date, revenue, expense));
		}

		return result;
	}
}
