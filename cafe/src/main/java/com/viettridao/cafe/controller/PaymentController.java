package com.viettridao.cafe.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.viettridao.cafe.dto.request.Pay.PaymentItemRequest;
import com.viettridao.cafe.dto.request.Pay.PaymentRequest;
import com.viettridao.cafe.dto.response.Pay.PaymentResponse;
import com.viettridao.cafe.dto.response.tables.TableMenuItemResponse;
import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.service.PaymentService;
import com.viettridao.cafe.service.TableService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

	private final TableService tableService;
	private final PaymentService paymentService;

	@GetMapping("/{tableId}")
	public String showPaymentForm(@PathVariable Integer tableId, Model model) {
		List<TableMenuItemResponse> items = tableService.getTableMenuItems(tableId);
		double total = items.stream().mapToDouble(item -> item.getAmount() != null ? item.getAmount() : 0.0).sum();

		PaymentRequest request = new PaymentRequest();
		request.setTableId(tableId);
		request.setCustomerCash(0.0);
		request.setFreeTable(true);

		List<PaymentItemRequest> itemRequests = items.stream().map(item -> {
			PaymentItemRequest reqItem = new PaymentItemRequest();
			reqItem.setMenuItemId(item.getMenuItemId());
			reqItem.setQuantity(item.getQuantity());
			reqItem.setPrice(item.getPrice());
			reqItem.setAmount(item.getAmount());
			return reqItem;
		}).toList();
		request.setItems(itemRequests);

		ReservationEntity reservation = tableService.getLatestReservationByTableId(tableId);
		String employeeName = (reservation != null && reservation.getEmployee() != null)
				? reservation.getEmployee().getFullName()
				: "Không xác định";

		model.addAttribute("tableId", tableId);
		model.addAttribute("menuItems", items);
		model.addAttribute("totalAmount", total);
		model.addAttribute("employeeName", employeeName);
		model.addAttribute("paymentRequest", request);

		return "sale/form";
	}

	@PostMapping("/process")
	public String processPayment(@Valid @ModelAttribute("paymentRequest") PaymentRequest request,
			BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			List<TableMenuItemResponse> items = tableService.getTableMenuItems(request.getTableId());
			double total = items.stream().mapToDouble(item -> item.getAmount() != null ? item.getAmount() : 0.0).sum();

			List<PaymentItemRequest> itemRequests = items.stream().map(item -> {
				PaymentItemRequest reqItem = new PaymentItemRequest();
				reqItem.setMenuItemId(item.getMenuItemId());
				reqItem.setQuantity(item.getQuantity());
				reqItem.setPrice(item.getPrice());
				reqItem.setAmount(item.getAmount());
				return reqItem;
			}).toList();
			request.setItems(itemRequests);

			ReservationEntity reservation = tableService.getLatestReservationByTableId(request.getTableId());
			String employeeName = (reservation != null && reservation.getEmployee() != null)
					? reservation.getEmployee().getFullName()
					: "Không xác định";

			model.addAttribute("tableId", request.getTableId());
			model.addAttribute("menuItems", items);
			model.addAttribute("totalAmount", total);
			model.addAttribute("employeeName", employeeName);
			model.addAttribute("paymentRequest", request);

			return "sale/form";
		}

		try {
			PaymentResponse response = paymentService.processPayment(request);

			if (!Boolean.TRUE.equals(response.isSuccess())) {
				model.addAttribute("error", response.getMessage());

				List<TableMenuItemResponse> items = tableService.getTableMenuItems(request.getTableId());
				double total = items.stream().mapToDouble(item -> item.getAmount() != null ? item.getAmount() : 0.0)
						.sum();

				List<PaymentItemRequest> itemRequests = items.stream().map(item -> {
					PaymentItemRequest reqItem = new PaymentItemRequest();
					reqItem.setMenuItemId(item.getMenuItemId());
					reqItem.setQuantity(item.getQuantity());
					reqItem.setPrice(item.getPrice());
					reqItem.setAmount(item.getAmount());
					return reqItem;
				}).toList();
				request.setItems(itemRequests);

				ReservationEntity reservation = tableService.getLatestReservationByTableId(request.getTableId());
				String employeeName = (reservation != null && reservation.getEmployee() != null)
						? reservation.getEmployee().getFullName()
						: "Không xác định";

				model.addAttribute("tableId", request.getTableId());
				model.addAttribute("menuItems", items);
				model.addAttribute("totalAmount", total);
				model.addAttribute("employeeName", employeeName);
				model.addAttribute("paymentRequest", request);

				return "sale/form";
			}

			model.addAttribute("response", response);
			model.addAttribute("success", response.getMessage());
			model.addAttribute("tableId", request.getTableId()); 

			return "sale/result";

		} catch (Exception e) {
			model.addAttribute("error", "Đã xảy ra lỗi khi xử lý thanh toán.");

			List<TableMenuItemResponse> items = tableService.getTableMenuItems(request.getTableId());
			double total = items.stream().mapToDouble(item -> item.getAmount() != null ? item.getAmount() : 0.0).sum();

			List<PaymentItemRequest> itemRequests = items.stream().map(item -> {
				PaymentItemRequest reqItem = new PaymentItemRequest();
				reqItem.setMenuItemId(item.getMenuItemId());
				reqItem.setQuantity(item.getQuantity());
				reqItem.setPrice(item.getPrice());
				reqItem.setAmount(item.getAmount());
				return reqItem;
			}).toList();
			request.setItems(itemRequests);

			ReservationEntity reservation = tableService.getLatestReservationByTableId(request.getTableId());
			String employeeName = (reservation != null && reservation.getEmployee() != null)
					? reservation.getEmployee().getFullName()
					: "Không xác định";

			model.addAttribute("tableId", request.getTableId());
			model.addAttribute("menuItems", items);
			model.addAttribute("totalAmount", total);
			model.addAttribute("employeeName", employeeName);
			model.addAttribute("paymentRequest", request);

			return "sale/form";
		}
	}
}