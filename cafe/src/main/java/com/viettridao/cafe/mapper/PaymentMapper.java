package com.viettridao.cafe.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.request.Pay.PaymentRequest;
import com.viettridao.cafe.dto.response.Pay.PaymentResponse;
import com.viettridao.cafe.mapper.base.BaseMapper;
import com.viettridao.cafe.model.InvoiceEntity;

@Component
public class PaymentMapper extends BaseMapper<InvoiceEntity, PaymentRequest, PaymentResponse> {

	public PaymentMapper(ModelMapper modelMapper) {
		super(modelMapper, InvoiceEntity.class, PaymentRequest.class, PaymentResponse.class);
	}

	// Nếu bạn cần custom thêm mapping ngoài mặc định
	@Override
	public InvoiceEntity fromRequest(PaymentRequest request) {
		InvoiceEntity invoice = super.fromRequest(request);
		// Không gán trực tiếp totalAmount, status, v.v. nếu nó là logic xử lý phía
		// service
		return invoice;
	}

	@Override
	public PaymentResponse toDto(InvoiceEntity entity) {
		PaymentResponse response = new PaymentResponse();
		response.setInvoiceId(entity.getId());
		response.setTotalAmount(entity.getTotalAmount());
		// Các field khác như customerCash, change, message cần service set sau
		return response;
	}
}
