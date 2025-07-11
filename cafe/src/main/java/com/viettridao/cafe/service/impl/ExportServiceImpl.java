package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.model.ProductEntity;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import com.viettridao.cafe.service.ExportService;

import org.springframework.transaction.annotation.Transactional;

import com.viettridao.cafe.dto.request.export.CreateExportRequest;
import com.viettridao.cafe.dto.request.export.UpdateExportRequest;
import com.viettridao.cafe.mapper.ExportMapper;
import com.viettridao.cafe.model.ExportEntity;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.repository.ExportRepository;
import com.viettridao.cafe.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ExportServiceImpl implements ExportService {

    private final ExportRepository exportRepository;
    private final ExportMapper exportMapper;
    private final EmployeeRepository employeeRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void createExport(CreateExportRequest request) {
        // Validate & fetch liên kết trước
        EmployeeEntity employee = findEmployeeOrThrow(request.getEmployeeId());
        ProductEntity product = findProductOrThrow(request.getProductId());

        // Kiểm tra số lượng trong kho trước khi xuất
        Integer currentQuantity = product.getQuantity() != null ? product.getQuantity() : 0;
        if (currentQuantity < request.getQuantity()) {
            throw new IllegalArgumentException(
                String.format("Không đủ hàng trong kho! Số lượng hiện có: %d, số lượng cần xuất: %d", 
                    currentQuantity, request.getQuantity())
            );
        }

        // Cập nhật số lượng sản phẩm trong kho (XUẤT = GIẢM số lượng)
        product.setQuantity(currentQuantity - request.getQuantity());
        productRepository.save(product);

        // Dùng mapper để tạo entity và gán liên kết đã kiểm tra
        ExportEntity exportEntity = exportMapper.toEntity(request);
        exportEntity.setEmployee(employee);
        exportEntity.setProduct(product);

        // Tính toán tổng tiền tự động từ backend
        Double calculatedTotal = request.getUnitExportPrice() * request.getQuantity();
        exportEntity.setTotalAmount(calculatedTotal);

        exportRepository.save(exportEntity);
    }

    @Override
    public UpdateExportRequest getUpdateForm(Integer id) {
        ExportEntity exportEntity = findExportOrThrow(id);
        return exportMapper.toUpdateRequest(exportEntity);
    }

    @Override
    @Transactional
    public void updateExport(Integer id, UpdateExportRequest request) {
        ExportEntity exportEntity = findExportOrThrow(id);

        // Cập nhật dữ liệu chung
        exportMapper.updateEntityFromRequest(request, exportEntity);

        // Validate & cập nhật lại liên kết employee, product nếu cần
        exportEntity.setEmployee(findEmployeeOrThrow(request.getEmployeeId()));
        exportEntity.setProduct(findProductOrThrow(request.getProductId()));

        exportRepository.save(exportEntity);
    }

    @Override
    @Transactional
    public void deleteExport(Integer id) {
        ExportEntity exportEntity = findExportOrThrow(id);
        exportEntity.setIsDeleted(true);
        exportRepository.save(exportEntity);
    }

    // ======= PRIVATE SUPPORT METHODS =======

    private ExportEntity findExportOrThrow(Integer id) {
        return exportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn xuất"));
    }

    private EmployeeEntity findEmployeeOrThrow(Integer id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nhân viên không tồn tại!"));
    }

    private ProductEntity findProductOrThrow(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại!"));
    }
}