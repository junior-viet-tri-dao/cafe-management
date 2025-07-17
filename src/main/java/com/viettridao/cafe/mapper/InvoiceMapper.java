package com.viettridao.cafe.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.response.invoice.InvoiceResponse;
import com.viettridao.cafe.model.InvoiceEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InvoiceMapper {
    private final ModelMapper modelMapper;

    public InvoiceResponse convertToDto(InvoiceEntity invoiceEntity) {
        return modelMapper.map(invoiceEntity, InvoiceResponse.class);
    }

}
