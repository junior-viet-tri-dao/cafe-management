package com.viettridao.cafe.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.viettridao.cafe.dto.response.tables.TableMenuItemResponse;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.service.TableService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sale")
public class PrintController {

	private final TableService tableService;

	@GetMapping("/{tableId}/print")
	public String printInvoice(@PathVariable Integer tableId, Model model) {
		InvoiceEntity invoice = tableService.getLatestInvoiceByTableId(tableId);
		List<TableMenuItemResponse> items;
		double total = 0.0;

		if (invoice == null || invoice.getInvoiceDetails() == null) {
			items = List.of();
		} else {
			items = invoice.getInvoiceDetails().stream()
					.filter(detail -> (detail.getIsDeleted() == null || !detail.getIsDeleted())
							&& detail.getQuantity() != null && detail.getQuantity() > 0 && detail.getPrice() != null)
					.map(detail -> {
						TableMenuItemResponse dto = new TableMenuItemResponse();
						dto.setMenuItemId(detail.getMenuItem().getId());
						dto.setItemName(detail.getMenuItem().getItemName());
						dto.setQuantity(detail.getQuantity());
						dto.setPrice(detail.getPrice());
						dto.setAmount(detail.getPrice() * detail.getQuantity());
						return dto;
					}).toList();

			total = items.stream().mapToDouble(TableMenuItemResponse::getAmount).sum();
		}

		ReservationEntity reservation = tableService.getLatestReservationByTableId(tableId);
		String employeeName = (reservation != null && reservation.getEmployee() != null)
				? reservation.getEmployee().getFullName()
				: "Không xác định";

		model.addAttribute("items", items);
		model.addAttribute("totalAmount", total);
		model.addAttribute("employeeName", employeeName);
		model.addAttribute("tableId", tableId);

		return "sale/print";
	}
}
