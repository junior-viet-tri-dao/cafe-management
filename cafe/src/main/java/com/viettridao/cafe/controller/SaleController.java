package com.viettridao.cafe.controller;



//import aj.org.objectweb.asm.TypeReference;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettridao.cafe.dto.request.export.CreateExportRequest;
import com.viettridao.cafe.dto.request.imports.CreateImportRequest;
import com.viettridao.cafe.dto.request.reservation.CreateReservationRequest;
import com.viettridao.cafe.dto.request.table.CreateTableRequest;
import com.viettridao.cafe.dto.request.table.SplitItemRequest;
import com.viettridao.cafe.dto.response.reservation.ReservationResponse;
import com.viettridao.cafe.mapper.ReservationMapper;
import com.viettridao.cafe.model.ReservationEntity;
import com.viettridao.cafe.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/sale")
public class SaleController {
    private final TableService tableService;
    private final MenuItemService menuItemService;
    private  final ReservationService reservationService;
    private final ReservationMapper reservationMapper;

    @GetMapping("")
    public String listTable(Model model){
        model.addAttribute("tables", tableService.getAllTables());
        model.addAttribute("menus",  menuItemService.getAllMenuItems());
        return "/sale/list_table";
    }

    @GetMapping("/insert")
    public String showFormCreateTable(Model model) {
        model.addAttribute("table", new CreateTableRequest());
        return "/sale/create_table";
    }


    @PostMapping("/insert")
    public String createEmployee(@Valid @ModelAttribute("table") CreateTableRequest request, BindingResult result) {
        try{
            if (result.hasErrors()) {
                return "/sales/create_table";
            }
            tableService.createTable(request);
            return "redirect:/sale";
        }catch (Exception e){
            return "redirect:/sale/create";
        }
    }

    // xem chi tiết bàn
    @GetMapping("/view/{id}")
    @ResponseBody
    public ReservationResponse getTableDetail(@PathVariable("id") Integer id) {
        ReservationEntity reservation = reservationService.getReservationById(id);
        ReservationResponse response = reservationMapper.toResponse(reservation);
        return response;
    }

    @GetMapping("/reservation")
    public String showReservationForm(@RequestParam("tableId") Integer tableId, Model model) {
        CreateReservationRequest request = new CreateReservationRequest();
        request.setTableId(tableId);
        model.addAttribute("reservation", request);
        LocalDateTime now = LocalDateTime.now();
        String minDateTime = now.truncatedTo(ChronoUnit.MINUTES).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        model.addAttribute("minDateTime", minDateTime);
        return "/sale/book_table";
    }


    @PostMapping("/reservation")
    public String reserveTable(
            @Valid @ModelAttribute("reservation") CreateReservationRequest request,
            BindingResult result
    ) {
        if (result.hasErrors()) {

            return "redirect:/sale/reservation?tableId=" + request.getTableId();
        }
        try {
            reservationService.createReservation(request);
            return "redirect:/sale";
        } catch (Exception e) {
            return "redirect:/sale";
        }
    }

    @PostMapping("/cancel")
    public String cancelTable(
            @RequestParam("tableId") Integer tableId
    ) {
        try {
            tableService.cancelTable(tableId);

        } catch (Exception e) {
        }
        return "redirect:/sale";
    }


    @PostMapping("/select-menu")
    public String selectMenuForTable(
            @RequestParam("tableId") Integer tableId,
            @RequestParam("menuIds") List<Integer> menuIds,
            @RequestParam("quantities") List<Integer> quantities
    ) {
        try {
            tableService.selectMenusForTable(tableId, menuIds, quantities);

            return "redirect:/sale";
        } catch (Exception e) {

            return "redirect:/sale";
        }
    }

    @PostMapping("/payment")
    public String payment(
            @RequestParam("tableId") Integer tableId
    ) {
        try {
            tableService.payment(tableId);

            return "redirect:/sale";
        } catch (Exception e) {

            return "redirect:/sale";
        }
    }


    @PostMapping("/transfer")
    public String moveTable(
            @RequestParam("fromTableId") Integer fromTableId,
            @RequestParam("toTableId") Integer toTableId
    ) {
        try {
            tableService.moveTable(fromTableId, toTableId);

            return "redirect:/sale";
        } catch (Exception e) {

            return "redirect:/sale";
        }
    }

    @PostMapping("/merge")
    public String mergeTables(
            @RequestParam("sourceTableIds") List<Integer> sourceTableIds,
            @RequestParam("targetTableId") Integer targetTableId) {

        try {
            tableService.merge(sourceTableIds, targetTableId);

            return "redirect:/sale";
        } catch (Exception e) {

            return "redirect:/sale";
        }

    }


    @PostMapping("/split")
    public String splitTable(
            @RequestParam Integer fromTableId,
            @RequestParam Integer toTableId,
            @RequestParam String splitItemsJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<SplitItemRequest> items = mapper.readValue(splitItemsJson, new TypeReference<List<SplitItemRequest>>() {
            });

            tableService.splitTable(fromTableId, toTableId, items);


            return "redirect:/sale";
        } catch (Exception e) {

            return "redirect:/sale";
        }
    }
}


