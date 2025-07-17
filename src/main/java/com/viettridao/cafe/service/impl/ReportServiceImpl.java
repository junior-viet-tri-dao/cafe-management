package com.viettridao.cafe.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.viettridao.cafe.dto.response.report.ReportResponse;
import com.viettridao.cafe.repository.ReportRepository;
import com.viettridao.cafe.service.ReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

        private final ReportRepository reportRepository;

        private LocalDate toLocalDate(Object obj) {
                if (obj instanceof LocalDate)
                        return (LocalDate) obj;
                if (obj instanceof java.sql.Date)
                        return ((java.sql.Date) obj).toLocalDate();
                return null;
        }

        @Override
        public List<ReportResponse> getReportByDateRange(LocalDate startDate, LocalDate endDate) {
                LocalDateTime start = startDate.atStartOfDay();
                LocalDateTime end = endDate.atTime(23, 59, 59);
                // 1. Lấy danh sách ngày trong khoảng
                List<LocalDate> dates = startDate.datesUntil(endDate.plusDays(1)).toList();

                // 2. Lấy dữ liệu thu (doanh thu từ hóa đơn đã thanh toán)
                List<Object[]> revenueList = reportRepository.sumRevenueByDate(start, end);

                // 3. Lấy dữ liệu chi (thiết bị, nhập hàng, chi tiêu khác)
                List<Object[]> deviceExpenseList = reportRepository.sumDeviceExpenseByDate(startDate, endDate);
                List<Object[]> importExpenseList = reportRepository.sumImportExpenseByDate(startDate, endDate);
                List<Object[]> otherExpenseList = reportRepository.sumOtherExpenseByDate(startDate, endDate);

                // 4. Gộp dữ liệu theo ngày
                List<ReportResponse> result = new ArrayList<>();
                for (LocalDate date : dates) {
                        long revenue = revenueList.stream()
                                        .filter(arr -> date.equals(toLocalDate(arr[0])))
                                        .mapToLong(arr -> arr[1] != null ? ((Number) arr[1]).longValue() : 0)
                                        .sum();

                        long deviceExpense = deviceExpenseList.stream()
                                        .filter(arr -> date.equals(arr[0]))
                                        .mapToLong(arr -> arr[1] != null ? ((Number) arr[1]).longValue() : 0)
                                        .sum();

                        long importExpense = importExpenseList.stream()
                                        .filter(arr -> date.equals(arr[0]))
                                        .mapToLong(arr -> arr[1] != null ? ((Number) arr[1]).longValue() : 0)
                                        .sum();

                        long otherExpense = otherExpenseList.stream()
                                        .filter(arr -> date.equals(arr[0]))
                                        .mapToLong(arr -> arr[1] != null ? ((Number) arr[1]).longValue() : 0)
                                        .sum();

                        long totalExpense = deviceExpense + importExpense + otherExpense;

                        result.add(new ReportResponse(date, revenue, totalExpense));
                }
                return result;
        }
}
