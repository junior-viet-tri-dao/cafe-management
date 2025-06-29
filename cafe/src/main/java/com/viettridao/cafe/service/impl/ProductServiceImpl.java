package com.viettridao.cafe.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.viettridao.cafe.dto.request.product.CreateProductRequest;
import com.viettridao.cafe.dto.response.product.ProductResponse;
import com.viettridao.cafe.mapper.ProductMapper;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.model.UnitEntity;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.service.ProductService;
import com.viettridao.cafe.service.UnitService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UnitService unitService;
    private final ProductMapper productMapper;

    @Transactional
    @Override
    public ProductEntity createProduct(CreateProductRequest request) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductName(request.getProductName());
        productEntity.setQuantity(0);
        productEntity.setProductPrice(request.getProductPrice());

        UnitEntity unit = unitService.getUnitById(request.getUnitId());
        productEntity.setUnit(unit);

        return productRepository.save(productEntity);
    }

    @Override
    public ProductEntity getProductById(Integer id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy hàng hóa có id=" + id));
    }

    @Override
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAllByIsDeleted(false);
    }

    @Override
    public List<ProductResponse> getAll() {
        return productMapper.toDtoList(productRepository.findAllByIsDeleted(false));
    }
}
