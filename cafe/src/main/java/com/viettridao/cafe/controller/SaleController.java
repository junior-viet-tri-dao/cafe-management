package com.viettridao.cafe.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettridao.cafe.dto.request.reservation.ReservationCreateRequest;
import com.viettridao.cafe.dto.request.table.SplitItemRequest;
import com.viettridao.cafe.dto.request.table.TableRequest;
import com.viettridao.cafe.dto.response.reservation.ReservationResponse;
import com.viettridao.cafe.dto.response.table.TableResponse;
import com.viettridao.cafe.mapper.MenuItemMapper;
import com.viettridao.cafe.mapper.ReservationMapper;
import com.viettridao.cafe.mapper.TableMapper;
import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.service.MenuItemService;
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
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sale")
public class SaleController {
    private final TableService tableService;
    private final TableMapper tableMapper;
    private final ReservationService reservationService;
    private final MenuItemService menuItemService;
    private final MenuItemMapper menuItemMapper;
    private final ReservationMapper reservationMapper;

    @GetMapping("")
    public String home(Model model) {
        model.addAttribute("tables", tableMapper.toListResponse(tableService.getAllTables()));
        model.addAttribute("menus", menuItemMapper.toListMenuItemResponse(menuItemService.getAllMenuItems()));
        return "/sales/sale";
    }

    @GetMapping("/create")
    public String showFormCreate(Model model) {
        model.addAttribute("table", new TableRequest());
        return "/sales/create_table";
    }

    // thêm bàn
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

    // xem chi tiết bàn
    @GetMapping("/api/view/{id}")
    @ResponseBody
    public ReservationResponse getTableDetail(@PathVariable("id") Integer id) {
        ReservationEntity reservation = reservationService.getReservationById(id);
        ReservationResponse response = reservationMapper.toResponse(reservation);
        return response;
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

    // đặt bàn
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

    @PostMapping("/cancel")
    public String cancelTable(
            @RequestParam("tableId") Integer tableId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            tableService.cancelTable(tableId);
            redirectAttributes.addFlashAttribute("success", "Hủy bàn thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Hủy bàn thất bại: " + e.getMessage());
        }
        return "redirect:/sale";
    }

    @PostMapping("/select-menu")
    public String selectMenuForTable(
            @RequestParam("tableId") Integer tableId,
            @RequestParam("menuIds") List<Integer> menuIds,
            @RequestParam("quantities") List<Integer> quantities,
            RedirectAttributes redirectAttributes
    ) {
        try {
            tableService.selectMenusForTable(tableId, menuIds, quantities);
            redirectAttributes.addFlashAttribute("success", "Chọn thực đơn cho bàn thành công!");
            return "redirect:/sale";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Chọn thực đơn thất bại: " + e.getMessage());
            return "redirect:/sale";
        }
    }

    @PostMapping("/payment")
    public String payment(
            @RequestParam("tableId") Integer tableId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            tableService.payment(tableId);
            redirectAttributes.addFlashAttribute("success", "Thanh toán thành công!");
            return "redirect:/sale";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Thanh toán thất bại: " + e.getMessage());
            return "redirect:/sale";
        }
    }

    @PostMapping("/transfer")
    public String moveTable(
            @RequestParam("fromTableId") Integer fromTableId,
            @RequestParam("toTableId") Integer toTableId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            tableService.moveTable(fromTableId, toTableId);
            redirectAttributes.addFlashAttribute("success", "Chuyển bàn thành công!");
            return "redirect:/sale";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Chuyển bàn thất bại: " + e.getMessage());
            return "redirect:/sale";
        }
    }

    @PostMapping("/merge")
    public String mergeTables(
            @RequestParam("sourceTableIds") List<Integer> sourceTableIds,
            @RequestParam("targetTableId") Integer targetTableId,
            RedirectAttributes redirectAttributes) {

        try {
            tableService.merge(sourceTableIds, targetTableId);
            redirectAttributes.addFlashAttribute("success", "Chuyển bàn thành công!");
            return "redirect:/sale";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Chuyển bàn thất bại: " + e.getMessage());
            return "redirect:/sale";
        }

    }

    @PostMapping("/split")
    public String splitTable(
            @RequestParam Integer fromTableId,
            @RequestParam Integer toTableId,
            @RequestParam String splitItemsJson,
            RedirectAttributes redirectAttributes) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<SplitItemRequest> items = mapper.readValue(splitItemsJson, new TypeReference<>() {});

            tableService.splitTable(fromTableId, toTableId, items);

            redirectAttributes.addFlashAttribute("success", "Tách bàn thành công!");
            return "redirect:/sale";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Tách bàn thất bại: " + e.getMessage());
            return "redirect:/sale";
        }
    }




}
