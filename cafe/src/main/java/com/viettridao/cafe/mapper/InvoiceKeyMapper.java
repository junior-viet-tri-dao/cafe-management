package com.viettridao.cafe.mapper;

import org.mapstruct.Mapper;

import com.viettridao.cafe.dto.response.invoicekey.InvoiceKeyResponse;
import com.viettridao.cafe.model.InvoiceKey;

@Mapper(componentModel = "spring")
public interface InvoiceKeyMapper {

    InvoiceKeyResponse toResponse(InvoiceKey entity);

}
