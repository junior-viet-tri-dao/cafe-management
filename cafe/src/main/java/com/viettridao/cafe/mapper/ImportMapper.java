package com.viettridao.cafe.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.request.imports.CreateImportRequest;
import com.viettridao.cafe.dto.request.imports.UpdateImportRequest;
import com.viettridao.cafe.dto.response.imports.ImportResponse;
import com.viettridao.cafe.mapper.base.BaseMapper;
import com.viettridao.cafe.model.ImportEntity;

@Component
public class ImportMapper extends BaseMapper<ImportEntity, Object, ImportResponse> {
	public ImportMapper(ModelMapper modelMapper) {
		super(modelMapper, ImportEntity.class, Object.class, ImportResponse.class); // Object vì không dùng request của
																					// BaseMapper
	}

	public ImportEntity fromCreateRequest(CreateImportRequest request) {
		return modelMapper.map(request, ImportEntity.class);
	}

	public void updateEntityFromRequest(UpdateImportRequest request, ImportEntity entity) {
		modelMapper.map(request, entity);
	}

	@Override
	public ImportResponse toDto(ImportEntity entity) {
		ImportResponse res = super.toDto(entity);

		var product = entity.getProduct();
		if (product != null) {
			res.setProductId(product.getId());
			res.setProductName(product.getProductName());
			res.setProductPrice(product.getProductPrice());

			if (product.getUnit() != null) {
				res.setUnitName(product.getUnit().getUnitName());
			}
		}

		return res;
	}
}
