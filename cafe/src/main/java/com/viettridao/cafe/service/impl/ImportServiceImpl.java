package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.request.imports.CreateImportRequest;
import com.viettridao.cafe.dto.request.imports.UpdateImportRequest;
import com.viettridao.cafe.model.ImportEntity;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.repository.ImportRepository;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.service.ImportService;
import com.viettridao.cafe.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImportServiceImpl implements ImportService {
    private final ImportRepository importRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    @Transactional
    @Override
    public ImportEntity createImport(CreateImportRequest request) {
        ImportEntity importEntity = new ImportEntity();
        importEntity.setImportDate(request.getImportDate());
        importEntity.setQuantity(request.getQuantity());

        ProductEntity product = productService.getProductById(request.getProductId());
        product.setQuantity(product.getQuantity() + request.getQuantity());

        productRepository.save(product);
        importEntity.setProduct(product);

        return importRepository.save(importEntity);
    }

    @Transactional
    @Override
    public void updateImport(UpdateImportRequest request) {
        ImportEntity importEntity = getImportById(request.getId());
        importEntity.setImportDate(request.getImportDate());
        importEntity.setQuantity(request.getQuantity());

        ProductEntity product = productService.getProductById(request.getProductId());
        importEntity.setProduct(product);

        importRepository.save(importEntity);
    }

    @Override
    public ImportEntity getImportById(Integer id) {
        return importRepository.findById(id).orElseThrow(()-> new RuntimeException("Không tìm thấy đơn nhập có id=" + id));
    }
}
