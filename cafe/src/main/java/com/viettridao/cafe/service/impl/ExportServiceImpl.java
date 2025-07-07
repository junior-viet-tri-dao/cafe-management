package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.request.export.CreateExportRequest;
import com.viettridao.cafe.model.ExportEntity;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.repository.ExportRepository;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.service.ExportService;
import com.viettridao.cafe.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Triển khai Service cho thực thể ExportEntity.
 * Chịu trách nhiệm xử lý logic nghiệp vụ liên quan đến xuất kho (Export).
 */
@Service
@RequiredArgsConstructor
public class ExportServiceImpl implements ExportService {

    // Repository cho thực thể ExportEntity
    private final ExportRepository exportRepository;

    // Service cho thực thể ProductEntity
    private final ProductService productService;

    // Repository cho thực thể ProductEntity
    private final ProductRepository productRepository;

    /**
     * Tạo mới một bản ghi xuất kho.
     *
     * @param request Đối tượng chứa thông tin cần thiết để tạo bản ghi xuất kho
     *                mới.
     * @return Thực thể ExportEntity vừa được tạo.
     * @throws IllegalArgumentException Nếu số lượng sản phẩm không đủ để xuất kho.
     */
    @Transactional
    @Override
    public ExportEntity createExport(CreateExportRequest request) {
        ExportEntity exportEntity = new ExportEntity();
        exportEntity.setExportDate(request.getExportDate());
        exportEntity.setQuantity(request.getQuantity());

        // Lấy thông tin sản phẩm dựa trên ID
        ProductEntity product = productService.getProductById(request.getProductId());

        if (product.getQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException("Không đủ số lượng để xuất hàng");
        }

        // Cập nhật số lượng sản phẩm sau khi xuất kho
        product.setQuantity(product.getQuantity() - request.getQuantity());
        productRepository.save(product);

        exportEntity.setProduct(product);

        return exportRepository.save(exportEntity);
    }

}
