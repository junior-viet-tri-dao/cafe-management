package com.viettridao.cafe.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.viettridao.cafe.dto.request.product.ProductCreateRequest;
import com.viettridao.cafe.dto.request.product.ProductUpdateRequest;
import com.viettridao.cafe.dto.response.product.ProductResponse;
import com.viettridao.cafe.model.ProductEntity;

@Mapper(componentModel = "spring", uses = {UnitMapper.class})
public interface ProductMapper {

    @Mapping(target = "deleted", constant = "false")
    ProductEntity toEntity(ProductCreateRequest request);

    ProductResponse toResponse(ProductEntity entity);

    ProductUpdateRequest toUpdateRequest(ProductEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateFromRequest(ProductUpdateRequest request, @MappingTarget ProductEntity entity);
}
