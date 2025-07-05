package com.viettridao.cafe.mapper;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.request.product.ProductRequest;
import com.viettridao.cafe.dto.response.product.ProductResponse;
import com.viettridao.cafe.mapper.base.BaseMapper;
import com.viettridao.cafe.model.ExportEntity;
import com.viettridao.cafe.model.ImportEntity;
import com.viettridao.cafe.model.ProductEntity;

@Component
public class ProductMapper extends BaseMapper<ProductEntity, ProductRequest, ProductResponse> {

	public ProductMapper(ModelMapper modelMapper) {
		super(modelMapper, ProductEntity.class, ProductRequest.class, ProductResponse.class);
	}

	@Override
	public ProductResponse toDto(ProductEntity entity) {
		ProductResponse dto = super.toDto(entity);

		if (entity.getUnit() != null) {
			dto.setUnitName(entity.getUnit().getUnitName());
			dto.setUnitId(entity.getUnit().getId()); // ✅ MỚI: để <select th:field="*{unitId}"> hoạt động
		} else {
			dto.setUnitName("N/A");
			dto.setUnitId(null);
		}

		Double price = entity.getProductPrice() != null ? entity.getProductPrice() : 0.0;
		Integer quantity = entity.getQuantity() != null ? entity.getQuantity() : 0;
		dto.setTotalAmount(price * quantity);

		dto.setLatestImportDate(
				entity.getImports() != null
						? entity.getImports().stream().filter(i -> i.getIsDeleted() == null || !i.getIsDeleted())
								.map(ImportEntity::getImportDate).max(LocalDate::compareTo).orElse(null)
						: null);

		dto.setLatestExportDate(
				entity.getExports() != null
						? entity.getExports().stream().filter(e -> e.getIsDeleted() == null || !e.getIsDeleted())
								.map(ExportEntity::getExportDate).max(LocalDate::compareTo).orElse(null)
						: null);

		return dto;
	}
}
