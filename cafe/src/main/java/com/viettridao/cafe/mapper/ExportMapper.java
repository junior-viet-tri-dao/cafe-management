package com.viettridao.cafe.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.request.export.ExportRequest;
import com.viettridao.cafe.dto.response.exports.ExportResponse;
import com.viettridao.cafe.mapper.base.BaseMapper;
import com.viettridao.cafe.model.ExportEntity;

@Component
public class ExportMapper extends BaseMapper<ExportEntity, ExportRequest, ExportResponse> {

	public ExportMapper(ModelMapper modelMapper) {
		super(modelMapper, ExportEntity.class, ExportRequest.class, ExportResponse.class);
	}

	@Override
	public ExportEntity fromRequest(ExportRequest dto) {
		ExportEntity entity = new ExportEntity();
		entity.setExportDate(dto.getExportDate());
		entity.setQuantity(dto.getQuantity());
		return entity;
	}

	@Override
	public ExportResponse toDto(ExportEntity entity) {
		ExportResponse dto = super.toDto(entity);
		dto.setProductId(entity.getProduct().getId());
		dto.setProductName(entity.getProduct().getProductName());
		dto.setEmployeeName(entity.getEmployee().getFullName());
		return dto;
	}
}
