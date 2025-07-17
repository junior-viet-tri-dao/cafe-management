package com.viettridao.cafe.service;

import java.time.LocalDateTime;
import java.util.List;

import com.viettridao.cafe.dto.response.invoice.InvoiceResponse;
import com.viettridao.cafe.model.InvoiceEntity;

public interface InvoiceService {
    InvoiceEntity getByTableId(Integer TableId);

    void checkout(Integer tableId);

    List<InvoiceEntity> getAllByInvoiceDateBetween(LocalDateTime start, LocalDateTime end);
}
