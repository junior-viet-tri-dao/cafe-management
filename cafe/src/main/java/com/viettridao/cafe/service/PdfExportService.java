package com.viettridao.cafe.service;

import com.viettridao.cafe.model.InvoiceEntity;

public interface PdfExportService {
    byte[] exportInvoiceToPdf(InvoiceEntity invoice);
}
