package com.viettridao.cafe.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.dto.request.tables.TableBookingRequest;
import com.viettridao.cafe.dto.response.tables.TableBookingResponse;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.service.ReservationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/booking")
@RequiredArgsConstructor
public class ReservationController {

	private final ReservationService reservationService;
	private final EmployeeRepository employeeRepository;

	@GetMapping
	public String showBookingForm(@RequestParam("tableId") Integer tableId, Model model, Principal principal) {
		TableBookingRequest request = new TableBookingRequest();
		request.setTableId(tableId);

		try {
			String username = principal.getName();

			EmployeeEntity employee = employeeRepository.findByAccountUsername(username)
					.orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với username: " + username));

			request.setEmployeeId(employee.getId());
			model.addAttribute("employeeName", employee.getFullName());

		} catch (Exception ex) {
			model.addAttribute("error", "Không thể lấy thông tin nhân viên.");
			model.addAttribute("booking", request);
			return "booking/form";
		}

		model.addAttribute("booking", request);
		return "booking/form";
	}

	@PostMapping
	public String bookTable(@ModelAttribute("booking") @Valid TableBookingRequest request, BindingResult result,
			Model model, Principal principal, RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return "booking/form";
		}

		try {
			String username = principal.getName();
			EmployeeEntity employee = employeeRepository.findByAccountUsername(username)
					.orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên."));
			request.setEmployeeId(employee.getId());
			model.addAttribute("employeeName", employee.getFullName());

			TableBookingResponse response = reservationService.bookTable(request);

			if (!response.isSuccess()) {
				model.addAttribute("error", response.getMessage());
				return "booking/form";
			}

			redirectAttributes.addFlashAttribute("success", "Đặt bàn thành công.");
			return "redirect:/sale";

		} catch (Exception e) {
			model.addAttribute("error", "Đã xảy ra lỗi trong quá trình đặt bàn.");
			return "booking/form";
		}
	}
}
