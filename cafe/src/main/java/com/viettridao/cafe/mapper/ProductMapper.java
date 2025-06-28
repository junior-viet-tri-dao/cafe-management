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

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "imports", ignore = true)
    @Mapping(target = "exports", ignore = true)
    @Mapping(target = "menuDetails", ignore = true)
    ProductEntity toEntity(ProductCreateRequest request);

    @Mapping(source = "unit", target = "unit")
    ProductResponse toResponse(ProductEntity entity);

    ProductUpdateRequest toUpdateRequest(ProductEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateFromRequest(ProductUpdateRequest request, @MappingTarget ProductEntity entity);
}
