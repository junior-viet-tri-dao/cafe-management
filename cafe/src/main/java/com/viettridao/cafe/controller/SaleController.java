package com.viettridao.cafe.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.dto.request.tables.MenuItemSplitRequest;
import com.viettridao.cafe.dto.request.tables.MenuItemSplitWrapper;
import com.viettridao.cafe.dto.response.tables.TableMenuItemResponse;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.TableService;
import com.viettridao.cafe.service.TableSplitService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sale")
public class SaleController {

	private final TableRepository tableRepository;
	private final TableService tableService;
	private final TableSplitService tableSplitService;

	@GetMapping("/split")
	public String showSplitForm(@RequestParam("fromTableId") Integer fromTableId, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			TableEntity fromTable = tableRepository.findById(fromTableId)
					.orElseThrow(() -> new RuntimeException("Không tìm thấy bàn nguồn"));

			if (fromTable.getStatus() == TableStatus.RESERVED) {
				redirectAttributes.addFlashAttribute("error", "Bàn này đã được đặt trước, không thể tách.");
				return "redirect:/sale";
			}

			InvoiceEntity invoice = tableService.getLatestUnpaidInvoiceByTableId(fromTableId);
			if (invoice == null) {
				redirectAttributes.addFlashAttribute("error", "Bàn này không có hóa đơn chưa thanh toán");
				return "redirect:/sale";
			}

			List<TableMenuItemResponse> items = tableService.getTableMenuItems(fromTableId);
			List<TableEntity> allTables = tableRepository.findByIsDeletedFalse();

			MenuItemSplitWrapper wrapper = new MenuItemSplitWrapper();
			wrapper.setItems(new java.util.ArrayList<>());

			model.addAttribute("fromTableId", fromTableId);
			model.addAttribute("menuItems", items);
			model.addAttribute("allTables", allTables.stream().filter(t -> !t.getId().equals(fromTableId)).toList());
			model.addAttribute("splitWrapper", wrapper);

			return "sale/split-form";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Lỗi khi hiển thị form tách bàn: " + e.getMessage());
			return "redirect:/sale";
		}
	}

	@PostMapping("/split")
	public String splitTable(@RequestParam("fromTableId") Integer fromTableId,
			@RequestParam("toTableId") Integer toTableId, @RequestParam("customerName") String customerName,
			@RequestParam("customerPhone") String customerPhone,
			@Valid @ModelAttribute("splitWrapper") MenuItemSplitWrapper wrapper, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("error", "Danh sách món tách không hợp lệ. Vui lòng kiểm tra lại.");
			return "redirect:/sale/split?fromTableId=" + fromTableId;
		}

		if (customerName == null || customerName.trim().isEmpty() || customerName.length() > 30) {
			redirectAttributes.addFlashAttribute("error", "Tên khách hàng không hợp lệ.");
			return "redirect:/sale/split?fromTableId=" + fromTableId;
		}

		if (!customerPhone.matches("\\d{10,11}")) {
			redirectAttributes.addFlashAttribute("error", "Số điện thoại không hợp lệ. Chỉ gồm 10 hoặc 11 chữ số.");
			return "redirect:/sale/split?fromTableId=" + fromTableId;
		}

		try {
			TableEntity toTable = tableRepository.findById(toTableId)
					.orElseThrow(() -> new RuntimeException("Không tìm thấy bàn đích"));

			if (toTable.getStatus() != TableStatus.AVAILABLE) {
				redirectAttributes.addFlashAttribute("error", "Chỉ có thể tách sang bàn trống (AVAILABLE).");
				return "redirect:/sale/split?fromTableId=" + fromTableId;
			}

			List<MenuItemSplitRequest> validItems = wrapper.getItems().stream()
					.filter(i -> Boolean.TRUE.equals(i.getSelected()) && i.getQuantityToMove() != null
							&& i.getQuantityToMove() > 0)
					.toList();

			if (validItems.isEmpty()) {
				redirectAttributes.addFlashAttribute("error", "Vui lòng chọn ít nhất 1 món hợp lệ để tách.");
				return "redirect:/sale/split?fromTableId=" + fromTableId;
			}

			List<TableMenuItemResponse> currentItems = tableService.getTableMenuItems(fromTableId);
			for (MenuItemSplitRequest item : validItems) {
				int availableQty = currentItems.stream()
						.filter(menuItem -> menuItem.getMenuItemId().equals(item.getMenuItemId())).findFirst()
						.map(TableMenuItemResponse::getQuantity).orElse(0);

				if (item.getQuantityToMove() > availableQty) {
					redirectAttributes.addFlashAttribute("error",
							"Số lượng muốn tách vượt quá số lượng hiện có cho món: " + item.getMenuItemId());
					return "redirect:/sale/split?fromTableId=" + fromTableId;
				}
			}

			tableSplitService.splitTable(fromTableId, toTableId, validItems, customerName, customerPhone);
			redirectAttributes.addFlashAttribute("success", "Tách bàn thành công!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Lỗi khi tách bàn: " + e.getMessage());
		}

		return "redirect:/sale";
	}
}
