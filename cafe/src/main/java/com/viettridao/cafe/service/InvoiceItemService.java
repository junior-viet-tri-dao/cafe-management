package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.invoice.InvoiceItemListRequest;
import com.viettridao.cafe.dto.response.invoice.InvoiceItemResponse;

import java.util.List;

public interface InvoiceItemService {
    List<InvoiceItemResponse> addItemsToInvoice(InvoiceItemListRequest request);
}
