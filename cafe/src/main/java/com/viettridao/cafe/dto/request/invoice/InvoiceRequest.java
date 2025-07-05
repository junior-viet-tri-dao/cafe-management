package com.viettridao.cafe.dto.request.invoice;

import java.util.List;

import com.viettridao.cafe.dto.request.invoicedetail.InvoiceDetailRequest;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class InvoiceRequest {

    private Integer tableId;

    private List<InvoiceDetailRequest> invoiceDetails;

}
