package com.viettridao.cafe.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.viettridao.cafe.dto.request.Pay.PaymentRequest;
import com.viettridao.cafe.dto.response.Pay.PaymentResponse;
import com.viettridao.cafe.model.InvoiceEntity;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

	@Mapping(target = "id", ignore = true) 
	InvoiceEntity fromRequest(PaymentRequest request);

	@Mappings({ @Mapping(source = "id", target = "invoiceId"),
			@Mapping(source = "totalAmount", target = "totalAmount") })
	PaymentResponse toDto(InvoiceEntity entity);
}
