package com.viettridao.cafe.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.viettridao.cafe.dto.response.product.ProductResponse;
import com.viettridao.cafe.mapper.ProductMapper;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAllByIsDeletedFalse(pageable)
                .map(productMapper::convertToDto);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAllByIsDeletedFalse()
                .stream()
                .map(productMapper::convertToDto)
                .toList();
    }

    @Override
    public Page<ProductResponse> searchByName(Pageable pageable, String keyword) {
        return productRepository.searchByName(pageable, keyword)
                .map(productMapper::convertToDto);
    }

}
