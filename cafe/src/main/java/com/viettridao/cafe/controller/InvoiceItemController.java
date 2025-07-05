package com.viettridao.cafe.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.viettridao.cafe.dto.request.invoices.InvoiceItemListRequest;
import com.viettridao.cafe.dto.request.invoices.InvoiceItemRequest;
import com.viettridao.cafe.dto.response.invoices.InvoiceItemResponse;
import com.viettridao.cafe.model.MenuItemEntity;
import com.viettridao.cafe.repository.MenuItemRepository;
import com.viettridao.cafe.service.InvoiceItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/invoice")
@RequiredArgsConstructor
public class InvoiceItemController {

	private final InvoiceItemService invoiceItemService;
	private final MenuItemRepository menuItemRepository;

	@GetMapping("/item-form")
	public String showItemForm(@RequestParam("invoiceId") Integer invoiceId, Model model) {
		List<MenuItemEntity> menuItems = menuItemRepository.findByIsDeletedFalse();
		List<InvoiceItemRequest> itemRequests = new ArrayList<>();

		for (MenuItemEntity item : menuItems) {
			InvoiceItemRequest req = new InvoiceItemRequest();
			req.setInvoiceId(invoiceId);
			req.setMenuItemId(item.getId());
			req.setQuantity(0);
			itemRequests.add(req);
		}

		InvoiceItemListRequest listRequest = new InvoiceItemListRequest();
		listRequest.setItems(itemRequests);

		model.addAttribute("menuItems", menuItems);
		model.addAttribute("form", listRequest);

		return "sale/item-form";
	}

	@PostMapping("/item")
	public String addItems(@ModelAttribute("form") @Valid InvoiceItemListRequest request, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			model.addAttribute("menuItems", menuItemRepository.findByIsDeletedFalse());
			return "sale/item-form";
		}

		try {
			List<InvoiceItemResponse> items = invoiceItemService.addItemsToInvoice(request);
			model.addAttribute("success", "Thêm món thành công");
			model.addAttribute("items", items);
			return "redirect:/sale";
		} catch (Exception e) {
			model.addAttribute("error", "Đã xảy ra lỗi khi thêm món vào hóa đơn.");
			model.addAttribute("menuItems", menuItemRepository.findByIsDeletedFalse());
			return "sale/item-form";
		}
	}
}
