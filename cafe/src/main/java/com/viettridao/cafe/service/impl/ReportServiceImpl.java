package com.viettridao.cafe.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.viettridao.cafe.common.ReportType;
import com.viettridao.cafe.dto.response.reportstatistics.ReportItemResponse;
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

		for (LocalDate date = fromDate; !date.isAfter(toDate); date = date.plusDays(1)) {
			Double revenue = invoiceRepository.sumTotalAmountByDate(date);
			Double importCost = importRepository.sumTotalAmountByDate(date);
			Double exportCost = exportRepository.sumTotalExportAmountByDate(date);
			Double otherExpenses = expenseRepository.sumAmountByDate(date);
			Double salary = employeeRepository.sumAllSalaries();

			double totalRevenue = revenue != null ? revenue : 0.0;
			double totalExpense = 0.0;

			totalExpense += importCost != null ? importCost : 0.0;
			totalExpense += exportCost != null ? exportCost : 0.0;
			totalExpense += otherExpenses != null ? otherExpenses : 0.0;
			totalExpense += salary != null ? salary : 0.0;

			reports.add(new ReportItemResponse(date, totalRevenue, totalExpense));
		}

		return reports;
	}

	@Override
	public List<ReportItemResponse> getReport(LocalDate fromDate, LocalDate toDate, ReportType type) {
		List<ReportItemResponse> result = new ArrayList<>();

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
				Double s = employeeRepository.sumAllSalaries();

				expense += i != null ? i : 0.0;
				expense += e != null ? e : 0.0;
				expense += o != null ? o : 0.0;
				expense += s != null ? s : 0.0;
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
			case SALARY, EMPLOYEE_INFO -> {
				Double s = employeeRepository.sumAllSalaries();
				expense += s != null ? s : 0.0;
			}
			}

			result.add(new ReportItemResponse(date, revenue, expense));
		}

		return result;
	}
}
