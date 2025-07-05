package com.viettridao.cafe.dto.request.invoices;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceItemRequest {

	@NotNull(message = "Mã hóa đơn không được để trống")
	private Integer invoiceId;

	@NotNull(message = "Mã món không được để trống")
	private Integer menuItemId;

	private Integer quantity;
}
