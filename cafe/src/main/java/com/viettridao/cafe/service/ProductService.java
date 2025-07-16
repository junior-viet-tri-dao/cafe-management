package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.product.CreateProductRequest;
import com.viettridao.cafe.dto.request.product.UpdateProductRequest;
import com.viettridao.cafe.dto.response.product.ProductResponsePage;
import com.viettridao.cafe.model.ProductEntity;
import jakarta.validation.Valid;

import java.util.List;

public interface ProductService {

    ProductEntity getProductById(Integer id);


    ProductResponsePage getAllProductPage(String keyword, int page, int size);

    ProductEntity updateProduct(UpdateProductRequest request);

    void createProduct(CreateProductRequest request);

    List<ProductEntity> getAllProduct();
}
