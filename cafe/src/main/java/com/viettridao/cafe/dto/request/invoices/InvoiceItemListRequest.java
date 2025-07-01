package com.viettridao.cafe.dto.request.invoices;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceItemListRequest {

	@NotEmpty(message = "Danh sách món không được để trống")
	@Valid
	private List<InvoiceItemRequest> items;
}
