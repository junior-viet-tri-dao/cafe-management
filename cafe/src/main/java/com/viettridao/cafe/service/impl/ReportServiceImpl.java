
package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.response.report.ReportItemResponse;
import com.viettridao.cafe.model.ExpenseEntity;
import com.viettridao.cafe.model.ExportEntity;
import com.viettridao.cafe.model.ImportEntity;
import com.viettridao.cafe.repository.ExpenseRepository;
import com.viettridao.cafe.repository.ExportRepository;
import com.viettridao.cafe.repository.ImportRepository;
import com.viettridao.cafe.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ExpenseRepository expenseRepository;
    private final ImportRepository importRepository;
    private final ExportRepository exportRepository;

    @Override
    public List<ReportItemResponse> getReport(LocalDate fromDate, LocalDate toDate, String category) {
        // Lấy dữ liệu chi (Expense)
        List<ExpenseEntity> expenses = expenseRepository.findAllByExpenseDateBetweenAndIsDeletedFalse(fromDate, toDate);
        // Lấy dữ liệu nhập (Import)
        List<ImportEntity> imports;
        if (fromDate != null && toDate != null) {
            imports = importRepository.findAll().stream()
                    .filter(i -> i.getImportDate() != null && !i.getImportDate().isBefore(fromDate)
                            && !i.getImportDate().isAfter(toDate))
                    .collect(Collectors.toList());
        } else {
            imports = importRepository.findAll();
        }
        // Lấy dữ liệu xuất (Export)
        List<ExportEntity> exports = exportRepository.findAllByExportDateBetween(fromDate, toDate);

        // Gom nhóm theo ngày
        Map<LocalDate, ReportItemResponse> map = new TreeMap<>();
        for (ExpenseEntity e : expenses) {
            map.computeIfAbsent(e.getExpenseDate(), d -> new ReportItemResponse(d, 0, 0));
            map.get(e.getExpenseDate()).setChi(
                    map.get(e.getExpenseDate()).getChi() + (e.getAmount() != null ? e.getAmount().longValue() : 0));
        }
        for (ImportEntity i : imports) {
            map.computeIfAbsent(i.getImportDate(), d -> new ReportItemResponse(d, 0, 0));
            // Nếu là "import" hoặc "import_export" thì luôn cộng vào chi
            // Nếu là "all" thì chỉ cộng import vào chi nếu ngày đó chưa có expense
            boolean isAll = "all".equals(category);
            boolean isImport = "import".equals(category) || "import_export".equals(category);
            boolean hasExpense = expenses.stream()
                    .anyMatch(e -> e.getExpenseDate() != null && e.getExpenseDate().equals(i.getImportDate()));
            if (isImport || (isAll && !hasExpense)) {
                map.get(i.getImportDate()).setChi(map.get(i.getImportDate()).getChi()
                        + (i.getQuantity() != null && i.getProduct() != null && i.getProduct().getProductPrice() != null
                                ? i.getQuantity() * i.getProduct().getProductPrice().longValue()
                                : 0));
            }
        }
        for (ExportEntity ex : exports) {
            map.computeIfAbsent(ex.getExportDate(), d -> new ReportItemResponse(d, 0, 0));
            if ("export".equals(category) || "import_export".equals(category) || "all".equals(category)) {
                map.get(ex.getExportDate())
                        .setThu(map.get(ex.getExportDate()).getThu() + (ex.getQuantity() != null
                                && ex.getProduct() != null && ex.getProduct().getProductPrice() != null
                                        ? ex.getQuantity() * ex.getProduct().getProductPrice().longValue()
                                        : 0));
            }
        }
        // Có thể bổ sung các loại khác như lương, phí khác, ...
        return new ArrayList<>(map.values());
    }

    @Override
    public long getTongThu(List<ReportItemResponse> reportList) {
        return reportList.stream().mapToLong(ReportItemResponse::getThu).sum();
    }

    @Override
    public long getTongChi(List<ReportItemResponse> reportList) {
        return reportList.stream().mapToLong(ReportItemResponse::getChi).sum();
    }
}
