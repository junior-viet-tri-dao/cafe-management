package com.viettridao.cafe.service.exports;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import com.viettridao.cafe.dto.request.export.ExportCreateRequest;
import com.viettridao.cafe.dto.request.export.ExportUpdateRequest;
import com.viettridao.cafe.mapper.ExportMapper;
import com.viettridao.cafe.model.ExportEntity;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.repository.ExportRepository;
import com.viettridao.cafe.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ExportServiceImpl implements IExportService {

    private final ExportRepository exportRepository;
    private final ExportMapper exportMapper;
    private final EmployeeRepository employeeRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void createExport(ExportCreateRequest request) {
        ExportEntity entity = exportMapper.toEntity(request);
        entity.setEmployee(
                employeeRepository.findById(request.getEmployeeId())
                        .orElseThrow(() -> new RuntimeException("Employee not found"))
        );
        entity.setProduct(
                productRepository.findById(request.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found"))
        );
        exportRepository.save(entity);
    }

    @Override
    public ExportUpdateRequest getUpdateForm(Integer id) {
        System.out.println("Getting update form for export ID: " + id);
        if (id == null) {
            throw new IllegalArgumentException("Export ID must not be null");
        }
        return exportMapper.toUpdateRequest(findExportOrThrow(id));
    }

    @Override
    @Transactional
    public void updateExport(Integer id, ExportUpdateRequest request) {
        System.out.println("Updating export with ID: " + id);
        System.out.println("Request: " + request);
        if (id == null) {
            throw new IllegalArgumentException("Export ID must not be null");
        }
        ExportEntity existing = findExportOrThrow(id);
        exportMapper.updateEntityFromRequest(request, existing);
        existing.setProduct(
                productRepository.findById(request.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found"))
        );
        existing.setEmployee(
                employeeRepository.findById(request.getEmployeeId())
                        .orElseThrow(() -> new RuntimeException("Employee not found"))
        );
        exportRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteExport(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Export ID must not be null");
        }
        ExportEntity entity = findExportOrThrow(id);
        entity.setDeleted(true);
        exportRepository.save(entity);
    }

    private ExportEntity findExportOrThrow(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Export ID must not be null");
        }
        return exportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn xuất với id = " + id));
    }
}