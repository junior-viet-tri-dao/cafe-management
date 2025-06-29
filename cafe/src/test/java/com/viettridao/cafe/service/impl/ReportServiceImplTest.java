package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.response.report.ReportItemResponse;
import com.viettridao.cafe.model.ExpenseEntity;
import com.viettridao.cafe.model.ExportEntity;
import com.viettridao.cafe.model.ImportEntity;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.repository.ExpenseRepository;
import com.viettridao.cafe.repository.ExportRepository;
import com.viettridao.cafe.repository.ImportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportServiceImplTest {
    @Mock
    ExpenseRepository expenseRepository;
    @Mock
    ImportRepository importRepository;
    @Mock
    ExportRepository exportRepository;
    @InjectMocks
    ReportServiceImpl reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetReport_NoData() {
        when(expenseRepository.findAllByExpenseDateBetweenAndIsDeletedFalse(any(), any()))
                .thenReturn(Collections.emptyList());
        when(importRepository.findAll()).thenReturn(Collections.emptyList());
        when(exportRepository.findAllByExportDateBetween(any(), any())).thenReturn(Collections.emptyList());
        List<ReportItemResponse> result = reportService.getReport(LocalDate.now(), LocalDate.now(), "all");
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetReport_ExpenseOnly() {
        ExpenseEntity e = new ExpenseEntity();
        e.setExpenseDate(LocalDate.of(2025, 6, 1));
        e.setAmount(1000.0);
        when(expenseRepository.findAllByExpenseDateBetweenAndIsDeletedFalse(any(), any())).thenReturn(List.of(e));
        when(importRepository.findAll()).thenReturn(Collections.emptyList());
        when(exportRepository.findAllByExportDateBetween(any(), any())).thenReturn(Collections.emptyList());
        List<ReportItemResponse> result = reportService.getReport(LocalDate.of(2025, 6, 1), LocalDate.of(2025, 6, 1),
                "all");
        assertEquals(1, result.size());
        assertEquals(1000, result.get(0).getChi());
        assertEquals(0, result.get(0).getThu());
    }

    @Test
    void testGetReport_ImportOnly() {
        ImportEntity i = new ImportEntity();
        i.setImportDate(LocalDate.of(2025, 6, 2));
        i.setQuantity(2);
        ProductEntity p = new ProductEntity();
        p.setProductPrice(500.0);
        i.setProduct(p);
        when(expenseRepository.findAllByExpenseDateBetweenAndIsDeletedFalse(any(), any()))
                .thenReturn(Collections.emptyList());
        when(importRepository.findAll()).thenReturn(List.of(i));
        when(exportRepository.findAllByExportDateBetween(any(), any())).thenReturn(Collections.emptyList());
        List<ReportItemResponse> result = reportService.getReport(LocalDate.of(2025, 6, 2), LocalDate.of(2025, 6, 2),
                "import");
        assertEquals(1, result.size());
        assertEquals(1000, result.get(0).getChi());
        assertEquals(0, result.get(0).getThu());
    }

    @Test
    void testGetReport_ExportOnly() {
        ExportEntity ex = new ExportEntity();
        ex.setExportDate(LocalDate.of(2025, 6, 3));
        ex.setQuantity(3);
        ProductEntity p = new ProductEntity();
        p.setProductPrice(200.0);
        ex.setProduct(p);
        when(expenseRepository.findAllByExpenseDateBetweenAndIsDeletedFalse(any(), any()))
                .thenReturn(Collections.emptyList());
        when(importRepository.findAll()).thenReturn(Collections.emptyList());
        when(exportRepository.findAllByExportDateBetween(any(), any())).thenReturn(List.of(ex));
        List<ReportItemResponse> result = reportService.getReport(LocalDate.of(2025, 6, 3), LocalDate.of(2025, 6, 3),
                "export");
        assertEquals(1, result.size());
        assertEquals(600, result.get(0).getThu());
        assertEquals(0, result.get(0).getChi());
    }

    @Test
    void testGetReport_AllTypes() {
        ExpenseEntity e = new ExpenseEntity();
        e.setExpenseDate(LocalDate.of(2025, 6, 4));
        e.setAmount(100.0);
        ImportEntity i = new ImportEntity();
        i.setImportDate(LocalDate.of(2025, 6, 4));
        i.setQuantity(1);
        ProductEntity pi = new ProductEntity();
        pi.setProductPrice(50.0);
        i.setProduct(pi);
        ExportEntity ex = new ExportEntity();
        ex.setExportDate(LocalDate.of(2025, 6, 4));
        ex.setQuantity(2);
        ProductEntity pe = new ProductEntity();
        pe.setProductPrice(30.0);
        ex.setProduct(pe);
        when(expenseRepository.findAllByExpenseDateBetweenAndIsDeletedFalse(any(), any())).thenReturn(List.of(e));
        when(importRepository.findAll()).thenReturn(List.of(i));
        when(exportRepository.findAllByExportDateBetween(any(), any())).thenReturn(List.of(ex));
        List<ReportItemResponse> result = reportService.getReport(LocalDate.of(2025, 6, 4), LocalDate.of(2025, 6, 4),
                "all");
        assertEquals(1, result.size());
        assertEquals(100, result.get(0).getChi());
        assertEquals(60, result.get(0).getThu());
    }
}
