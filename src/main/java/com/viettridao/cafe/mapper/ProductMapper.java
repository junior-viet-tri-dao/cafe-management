package com.viettridao.cafe.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.response.product.ProductResponse;

import com.viettridao.cafe.model.ProductEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final ModelMapper modelMapper;

    public ProductResponse convertToDto(ProductEntity product) {
        return modelMapper.map(product, ProductResponse.class);
    }
}
