package com.viettridao.cafe.service;

import java.util.List;

import com.viettridao.cafe.dto.request.product.CreateProductRequest;
import com.viettridao.cafe.dto.response.product.ProductResponse;
import com.viettridao.cafe.model.ProductEntity;

public interface ProductService {
    ProductEntity createProduct(CreateProductRequest request);
    ProductEntity getProductById(Integer id);
    List<ProductEntity> getAllProducts();
    List<ProductResponse> getAll(); 
}
