package com.viettridao.cafe.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.viettridao.cafe.dto.request.import_request.ImportRequest;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.ImportEntity;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.repository.ImportRepository;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.service.ImportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImportServiceImpl implements ImportService {

    private final EmployeeRepository employeeRepository;
    private final ProductRepository productRepository;
    private final ImportRepository importRepository;

    @Override
    public ImportEntity createImport(ImportRequest request, Integer employeeId) {
        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        EmployeeEntity employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        ImportEntity importEntity = new ImportEntity();
        importEntity.setImportDate(request.getImportDate());
        importEntity.setQuantity(request.getQuantity());
        importEntity.setIsDeleted(false);
        importEntity.setEmployee(employee);
        importEntity.setProduct(product);
        importEntity.setTotalAmount(product.getPrice().multiply(java.math.BigDecimal.valueOf(request.getQuantity())));

        product.setQuantity(product.getQuantity() + request.getQuantity());
        productRepository.save(product);

        return importRepository.save(importEntity);
    }

    @Override
    public List<ImportEntity> getAllByImportDateRange(LocalDate startDate, LocalDate endDate) {
        return importRepository.findAllByImportDateRange(startDate, endDate);
    }

}
