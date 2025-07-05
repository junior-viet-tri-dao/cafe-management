package com.viettridao.cafe.service.invoice_detail;

import com.viettridao.cafe.dto.request.invoice.InvoiceRequest;

public interface IInvoiceDetailService {

    void createMenusForInvoice(InvoiceRequest request);
}
