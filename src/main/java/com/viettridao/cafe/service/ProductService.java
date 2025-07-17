package com.viettridao.cafe.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.viettridao.cafe.dto.response.product.ProductResponse;

public interface ProductService {
    Page<ProductResponse> getAllProducts(Pageable pageable);

    Page<ProductResponse> searchByName(Pageable pageable, String keyword);

    List<ProductResponse> getAllProducts();
}
