package com.viettridao.cafe.dto.request.invoice;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequest {
    private Integer tableId;
    private List<InvoiceDetailRequest> invoiceDetails;
}
