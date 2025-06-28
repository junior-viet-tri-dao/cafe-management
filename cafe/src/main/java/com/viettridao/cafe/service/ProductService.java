package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.product.CreateProductRequest;
import com.viettridao.cafe.model.ProductEntity;

import java.util.List;

public interface ProductService {
    ProductEntity createProduct(CreateProductRequest request);
    ProductEntity getProductById(Integer id);
    List<ProductEntity> getAllProducts();
}
