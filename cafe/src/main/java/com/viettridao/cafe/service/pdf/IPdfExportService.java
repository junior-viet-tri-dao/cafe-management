package com.viettridao.cafe.service.pdf;

import com.viettridao.cafe.model.InvoiceEntity;

public interface IPdfExportService {

    byte[] exportInvoiceToPdf(InvoiceEntity invoice);
}
