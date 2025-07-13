package com.viettridao.cafe.mapper;

import java.util.Comparator;
import java.util.List;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.viettridao.cafe.dto.request.product.ProductRequest;
import com.viettridao.cafe.dto.response.product.ProductResponse;
import com.viettridao.cafe.model.ExportEntity;
import com.viettridao.cafe.model.ImportEntity;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.model.UnitEntity;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {

	@Mapping(target = "id", ignore = true) 
	@Mapping(target = "unit", ignore = true)
	@Mapping(target = "imports", ignore = true)
	@Mapping(target = "exports", ignore = true)
	public abstract ProductEntity fromRequest(ProductRequest request);

	@Mapping(target = "unitId", source = "unit.id")
	@Mapping(target = "unitName", source = "unit.unitName")
	@Mapping(target = "latestPrice", ignore = true)
	@Mapping(target = "lastImportDate", ignore = true)
	@Mapping(target = "lastExportDate", ignore = true)
	public abstract ProductResponse toDto(ProductEntity entity);

	@AfterMapping
	protected void afterMappingRequest(ProductRequest request, @MappingTarget ProductEntity entity) {
		if (request.getUnitId() != null) {
			UnitEntity unit = new UnitEntity();
			unit.setId(request.getUnitId());
			entity.setUnit(unit);
		}
	}

	@AfterMapping
	protected void afterMappingToDto(ProductEntity entity, @MappingTarget ProductResponse response) {
		List<ImportEntity> imports = entity.getImports();
		if (imports != null && !imports.isEmpty()) {
			imports.stream().filter(i -> !Boolean.TRUE.equals(i.getIsDeleted()))
					.max(Comparator.comparing(ImportEntity::getImportDate)).ifPresent(latest -> {
						response.setLatestPrice(latest.getPrice());
						response.setLastImportDate(latest.getImportDate());
					});
		}

		List<ExportEntity> exports = entity.getExports();
		if (exports != null && !exports.isEmpty()) {
			exports.stream().filter(e -> !Boolean.TRUE.equals(e.getIsDeleted()))
					.max(Comparator.comparing(ExportEntity::getExportDate))
					.ifPresent(latest -> response.setLastExportDate(latest.getExportDate()));
		}
	}

	@BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(target = "unit", ignore = true)
	public abstract void updateEntityFromRequest(ProductRequest request, @MappingTarget ProductEntity entity);
}
