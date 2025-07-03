package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.request.employee.CreateEmployeeRequest;
import com.viettridao.cafe.dto.request.reservation.ReservationCreateRequest;
import com.viettridao.cafe.dto.request.table.TableRequest;
import com.viettridao.cafe.dto.response.table.TableResponse;
import com.viettridao.cafe.mapper.TableMapper;
import com.viettridao.cafe.service.ReservationService;
import com.viettridao.cafe.service.TableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sale")
public class SaleController {
    private final TableService tableService;
    private final TableMapper tableMapper;
    private final ReservationService reservationService;

    @GetMapping("")
    public String home(Model model) {
        model.addAttribute("tables", tableMapper.toListResponse(tableService.getAllTables()));
        return "/sales/sale";
    }

    @GetMapping("/create")
    public String showFormCreate(Model model) {
        model.addAttribute("table", new TableRequest());
        return "/sales/create_table";
    }

    @PostMapping("/create")
    public String createEmployee(@Valid @ModelAttribute("table") TableRequest table, BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        try{
            if (result.hasErrors()) {
                return "/sales/create_table";
            }
            tableService.create(table);
            redirectAttributes.addFlashAttribute("success", "Thêm bàn thành công");
            return "redirect:/sale";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/sale/create";
        }
    }
    
    @GetMapping("/api/view/{id}")
    @ResponseBody
    public TableResponse getTableDetail(@PathVariable Integer id) {
        return tableMapper.toResponse(tableService.getTableById(id));
    }

    @GetMapping("/reservation")
    public String showReservationForm(@RequestParam("tableId") Integer tableId, Model model) {
        ReservationCreateRequest request = new ReservationCreateRequest();
        request.setTableId(tableId);
        model.addAttribute("reservation", request);
        LocalDateTime now = LocalDateTime.now();
        String minDateTime = now.truncatedTo(ChronoUnit.MINUTES).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        model.addAttribute("minDateTime", minDateTime);
        return "/sales/book_table";
    }

    @PostMapping("/reservation")
    public String reserveTable(
            @Valid @ModelAttribute("reservation") ReservationCreateRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", result.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/sale/reservation?tableId=" + request.getTableId();
        }
        try {
            reservationService.createReservation(request);
            redirectAttributes.addFlashAttribute("success", "Đặt bàn thành công!");
            return "redirect:/sale";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đặt bàn thất bại: " + e.getMessage());
            return "redirect:/sale";
        }
    }
}
