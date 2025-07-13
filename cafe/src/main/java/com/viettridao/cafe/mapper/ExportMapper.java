package com.viettridao.cafe.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.viettridao.cafe.dto.request.export.ExportRequest;
import com.viettridao.cafe.dto.response.exports.ExportResponse;
import com.viettridao.cafe.model.ExportEntity;

@Mapper(componentModel = "spring")
public interface ExportMapper {

	@Mappings({ @Mapping(source = "product.id", target = "productId"),
			@Mapping(source = "product.productName", target = "productName"),
			@Mapping(target = "employeeName", expression = "java(entity.getEmployee() != null ? entity.getEmployee().getFullName() : \"Không xác định\")") })
	ExportResponse toDto(ExportEntity entity);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "product", ignore = true)
	@Mapping(target = "employee", ignore = true)
	ExportEntity fromRequest(ExportRequest request);
}
