package com.viettridao.cafe.service;

import java.util.List;

import com.viettridao.cafe.dto.response.product.ProductResponse;

public interface ProductService {
    List<ProductResponse> getAllProducts();
}
