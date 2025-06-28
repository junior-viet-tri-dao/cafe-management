package com.viettridao.cafe.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.request.product.CreateProductRequest;
import com.viettridao.cafe.dto.response.product.ProductResponse;
import com.viettridao.cafe.mapper.base.BaseMapper;
import com.viettridao.cafe.model.ProductEntity;

@Component
public class ProductMapper extends BaseMapper<ProductEntity, CreateProductRequest, ProductResponse> {

	public ProductMapper(ModelMapper modelMapper) {
		super(modelMapper, ProductEntity.class, CreateProductRequest.class, ProductResponse.class);
	}

	@Override
	public ProductResponse toDto(ProductEntity entity) {
		ProductResponse res = super.toDto(entity);

		var unit = entity.getUnit();
		if (unit != null) {
			res.setUnitName(unit.getUnitName());
		}

		return res;
	}

	@Override
	public List<ProductResponse> toDtoList(List<ProductEntity> entities) {
		return entities.stream().map(this::toDto).toList();
	}
}
