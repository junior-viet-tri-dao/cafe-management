package com.viettridao.cafe.service;

import com.viettridao.cafe.model.InvoiceEntity;

/**
 * PdfExportService
 *
 * Version 1.0
 *
 * Date: 18-07-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE         AUTHOR      DESCRIPTION
 * -------------------------------------------------------
 * 18-07-2025   mirodoan    Create
 */
public interface PdfExportService {
    /**
     * Xuất hóa đơn ra file PDF.
     *
     * @param invoice Hóa đơn cần xuất PDF.
     * @return Mảng byte của file PDF được sinh ra.
     */
    byte[] exportInvoiceToPdf(InvoiceEntity invoice);
}