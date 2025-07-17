package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.invoice.InvoiceRequest;

public interface InvoiceDetailService {

    void createInvoiceDetail(InvoiceRequest request, Integer employeeId);
}
