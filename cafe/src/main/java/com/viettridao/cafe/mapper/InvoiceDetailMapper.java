package com.viettridao.cafe.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.request.invoices.InvoiceItemRequest;
import com.viettridao.cafe.dto.response.invoices.InvoiceItemResponse;
import com.viettridao.cafe.mapper.base.BaseMapper;
import com.viettridao.cafe.model.InvoiceDetailEntity;
import com.viettridao.cafe.model.InvoiceKey;

@Component
public class InvoiceDetailMapper extends BaseMapper<InvoiceDetailEntity, InvoiceItemRequest, InvoiceItemResponse> {

	public InvoiceDetailMapper(ModelMapper modelMapper) {
		super(modelMapper, InvoiceDetailEntity.class, InvoiceItemRequest.class, InvoiceItemResponse.class);
	}

	@Override
	public InvoiceItemResponse toDto(InvoiceDetailEntity entity) {
		InvoiceItemResponse response = new InvoiceItemResponse();

		response.setId(entity.getId().getIdInvoice());
		response.setItemName(entity.getMenuItem().getItemName());
		response.setQuantity(entity.getQuantity());
		response.setUnitPrice(entity.getPrice());
		response.setTotalPrice(entity.getQuantity() * entity.getPrice());

		return response;
	}

	@Override
	public InvoiceDetailEntity fromRequest(InvoiceItemRequest request) {
		InvoiceDetailEntity entity = new InvoiceDetailEntity();

		InvoiceKey key = new InvoiceKey();
		key.setIdInvoice(request.getInvoiceId());
		key.setIdMenuItem(request.getMenuItemId());
		entity.setId(key);

		entity.setQuantity(request.getQuantity());
		entity.setIsDeleted(false);

		return entity;
	}
}
