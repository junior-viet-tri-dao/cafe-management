package com.viettridao.cafe.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    // Hiển thị form đặt bàn
    @GetMapping
    public String showBookingForm(@RequestParam("tableId") Integer tableId,
                                  Model model,
                                  Principal principal) {
        TableBookingRequest request = new TableBookingRequest();
        request.setTableId(tableId);

        try {
            // ✅ Lấy username từ tài khoản đăng nhập
            String username = principal.getName();

            // ✅ Tìm nhân viên qua account.username
            EmployeeEntity employee = employeeRepository.findByAccountUsername(username)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với username: " + username));

            // ✅ Gán employeeId & hiển thị tên
            request.setEmployeeId(employee.getId());
            model.addAttribute("employeeName", employee.getFullName());

        } catch (Exception ex) {
            model.addAttribute("error", "Không thể lấy thông tin nhân viên.");
            return "booking/form";
        }

        model.addAttribute("booking", request);
        return "booking/form";
    }

    // Xử lý form đặt bàn
    @PostMapping
    public String bookTable(@ModelAttribute("booking") @Valid TableBookingRequest request,
                            BindingResult result,
                            Model model,
                            Principal principal) {

        if (result.hasErrors()) {
            return "booking/form";
        }

        try {
            // Gán lại nhân viên từ phiên đăng nhập (bảo vệ phía server)
            String username = principal.getName();
            EmployeeEntity employee = employeeRepository.findByAccountUsername(username)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên."));
            request.setEmployeeId(employee.getId());
            model.addAttribute("employeeName", employee.getFullName());

        } catch (Exception e) {
            model.addAttribute("error", "Lỗi xác định nhân viên đăng nhập.");
            return "booking/form";
        }

        TableBookingResponse response = reservationService.bookTable(request);

        if (!response.isSuccess()) {
            model.addAttribute("error", response.getMessage());
            return "booking/form";
        }

        return "redirect:/sale";
    }
}
