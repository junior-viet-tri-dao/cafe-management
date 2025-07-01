package com.viettridao.cafe.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.viettridao.cafe.dto.response.tables.TableMenuItemResponse;
import com.viettridao.cafe.dto.response.tables.TableResponse;
import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.service.TableService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/sale")
@RequiredArgsConstructor
public class TableController {

	private final TableService tableService;

	// ✅ Danh sách bàn (có phân trang)
	@GetMapping
	public String getAllTables(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			Model model) {

		// Ngăn truy cập âm
		if (page < 0) {
			return "redirect:/sale?page=0";
		}

		Page<TableResponse> tablePage = tableService.getAllTables(page, size);

		// Nếu vượt trang cuối
		if (page >= tablePage.getTotalPages() && tablePage.getTotalPages() > 0) {
			return "redirect:/sale?page=" + (tablePage.getTotalPages() - 1);
		}

		model.addAttribute("tables", tablePage.getContent());
		model.addAttribute("currentPage", tablePage.getNumber());
		model.addAttribute("totalPages", tablePage.getTotalPages());
		return "sale/list"; // templates/sale/list.html
	}

	// ✅ Xem món đã đặt + thông tin đặt trước (gồm tên nhân viên)
	@GetMapping("/{tableId}/menu")
	public String getTableMenu(@PathVariable Integer tableId, Model model) {
		// Danh sách món đã đặt (nếu có)
		List<TableMenuItemResponse> menuItems = tableService.getTableMenuItems(tableId);
		model.addAttribute("menuItems", menuItems);

		// Thông tin đặt trước (nếu có)
		ReservationEntity reservation = tableService.getLatestReservationByTableId(tableId);
		model.addAttribute("reservation", reservation);

		// ✅ Lấy trạng thái hóa đơn (nếu có món → có hóa đơn), ngược lại → "Chưa có hóa
		// đơn"
		String invoiceStatus;
		if (menuItems == null || menuItems.isEmpty()) {
			invoiceStatus = "Chưa có hóa đơn";
		} else {
			var invoice = tableService.getLatestUnpaidInvoiceByTableId(tableId);
			invoiceStatus = (invoice != null) ? invoice.getStatus().name() : "Chưa có hóa đơn";
		}
		model.addAttribute("invoiceStatus", invoiceStatus);

		return "sale/menu";
	}

	// ✅ Tạo hoặc lấy hóa đơn đang mở của bàn
	@GetMapping("/invoice/create-or-get")
	public String getOrCreateInvoice(@RequestParam("tableId") Integer tableId) {
		Integer invoiceId = tableService.getOrCreateInvoiceIdByTableId(tableId);
		return "redirect:/invoice/item-form?invoiceId=" + invoiceId;
	}
}
