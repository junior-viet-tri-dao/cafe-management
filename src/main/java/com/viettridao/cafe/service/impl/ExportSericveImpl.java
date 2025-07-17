package com.viettridao.cafe.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.viettridao.cafe.dto.request.export_request.ExportRequest;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.ExportEntity;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.repository.ExportRepository;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.service.ExportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExportSericveImpl implements ExportService {

    private final ExportRepository exportRepository;
    private final EmployeeRepository employeeRepository;
    private final ProductRepository productRepository;

    @Override
    public ExportEntity createExport(ExportRequest request, Integer employeeId) {

        ExportEntity exportEntity = new ExportEntity();

        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));

        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + request.getProductId()));

        exportEntity.setExportDate(request.getExportDate());
        exportEntity.setQuantity(request.getQuantity());
        exportEntity.setIsDeleted(false);
        exportEntity.setEmployee(employee);
        exportEntity.setProduct(product);
        exportEntity.setTotalExportAmount(null); // kiểm tra lại trường này sai
        product.setQuantity(product.getQuantity() - request.getQuantity());
        productRepository.save(product);

        return exportRepository.save(exportEntity);
    }

    @Override
    public List<ExportEntity> getAllByExportDateRange(LocalDate start, LocalDate end) {
        return exportRepository.findAllByExportDateBetween(start, end);
    }
}
