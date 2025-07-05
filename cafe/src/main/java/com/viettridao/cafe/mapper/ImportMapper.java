package com.viettridao.cafe.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.request.imports.ImportRequest;
import com.viettridao.cafe.dto.response.imports.ImportResponse;
import com.viettridao.cafe.mapper.base.BaseMapper;
import com.viettridao.cafe.model.ImportEntity;

@Component
public class ImportMapper extends BaseMapper<ImportEntity, ImportRequest, ImportResponse> {

	public ImportMapper(ModelMapper modelMapper) {
		super(modelMapper, ImportEntity.class, ImportRequest.class, ImportResponse.class);
	}

	@Override
	public ImportEntity fromRequest(ImportRequest dto) {
		ImportEntity entity = new ImportEntity();
		entity.setImportDate(dto.getImportDate());
		entity.setQuantity(dto.getQuantity());
		return entity;
	}

	@Override
	public ImportResponse toDto(ImportEntity entity) {
		ImportResponse dto = super.toDto(entity);
		dto.setProductId(entity.getProduct().getId());
		dto.setProductName(entity.getProduct().getProductName());
		dto.setEmployeeName(entity.getEmployee().getFullName());
		return dto;
	}
}
