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

@Service
@RequiredArgsConstructor
public class ExportServiceImpl implements ExportService{
    private final ExportRepository exportRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;

    @Transactional
    @Override
    public ExportEntity createExport(CreateExportRequest request) {
        ExportEntity exportEntity = new ExportEntity();
        exportEntity.setExportDate(request.getExportDate());
        exportEntity.setQuantity(request.getQuantity());

        ProductEntity product = productService.getProductById(request.getProductId());

        if (product.getQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException("Không đủ số lượng để xuất hàng");
        }

        product.setQuantity(product.getQuantity() - request.getQuantity());
        productRepository.save(product);

        exportEntity.setProduct(product);

        return exportRepository.save(exportEntity);
    }

}
