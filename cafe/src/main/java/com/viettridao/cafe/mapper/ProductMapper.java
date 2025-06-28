package com.viettridao.cafe.mapper;

import com.viettridao.cafe.dto.response.product.ProductResponse;
import com.viettridao.cafe.model.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductMapper {
    private final ModelMapper modelMapper;

    public ProductResponse toProductResponse(ProductEntity product){
        ProductResponse productResponse = new ProductResponse();
        modelMapper.map(product, productResponse);
//        productResponse.setId(product.getId());
//        productResponse.setProductName(product.getProductName());
//        productResponse.setQuantity(product.getQuantity());

        if(product.getUnit() != null){
            productResponse.setUnitName(product.getUnit().getUnitName());
        }
        return productResponse;
    }

    public List<ProductResponse> toProductResponse(List<ProductEntity> products){
        return products.stream().map(this::toProductResponse).toList();
    }
}
