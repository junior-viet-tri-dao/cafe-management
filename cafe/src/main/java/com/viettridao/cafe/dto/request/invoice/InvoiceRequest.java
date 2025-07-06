package com.viettridao.cafe.dto.request.invoice;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.viettridao.cafe.dto.request.invoicedetail.InvoiceDetailRequest;

@Getter
@Setter
public class InvoiceRequest {

    private Integer tableId;

    private List<InvoiceDetailRequest> invoiceDetails;

}
