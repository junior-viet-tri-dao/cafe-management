package com.viettridao.cafe.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.viettridao.cafe.dto.response.sales.MenuItemResponse;
import com.viettridao.cafe.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.viettridao.cafe.dto.request.sales.CreateReservationRequest;
import com.viettridao.cafe.dto.request.sales.CreateSelectMenuRequest;
import com.viettridao.cafe.dto.request.sales.MergeTableRequest;
import com.viettridao.cafe.dto.request.sales.SplitTableRequest;
import com.viettridao.cafe.dto.response.sales.OrderDetailRessponse;
import com.viettridao.cafe.mapper.OrderDetailMapper;
import com.viettridao.cafe.repository.AccountRepository;
import com.viettridao.cafe.repository.InvoiceDetailRepository;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.ReservationService;
import com.viettridao.cafe.service.SelectMenuService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * SalesController
 *
 * Version 1.0
 *
 * Date: 18-07-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE         AUTHOR      DESCRIPTION
 * -------------------------------------------------------
 * 18-07-2025   mirodoan    Create
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/sale")
public class SalesController {

    // Repository quản lý thông tin bàn và trạng thái
    private final TableRepository tableRepository;
    // Service xử lý logic đặt bàn, gộp bàn, tách bàn
    private final ReservationService reservationService;
    // Repository quản lý thông tin tài khoản và nhân viên
    private final AccountRepository accountRepository;
    // Service xử lý logic chọn thực đơn và tạo order
    private final SelectMenuService selectMenuService;
    // Repository quản lý chi tiết hóa đơn (invoice details)
    private final InvoiceDetailRepository invoiceDetailRepository;
    // Mapper chuyển đổi entity sang DTO response
    private final OrderDetailMapper orderDetailMapper;

    @GetMapping("")
    public String getSalesOverview(Model model) {
        // Chuẩn bị dữ liệu cơ bản cho view
        model.addAttribute("tables", tableRepository.findAll());
        model.addAttribute("reservation", new CreateReservationRequest());
        model.addAttribute("showReservationForm", false);

        // Đảm bảo các object này luôn tồn tại để tránh lỗi template
        if (!model.containsAttribute("selectMenuRequest")) {
            model.addAttribute("selectMenuRequest", new CreateSelectMenuRequest());
        }
        if (!model.containsAttribute("reservation")) {
            model.addAttribute("reservation", new CreateReservationRequest());
        }

        return "sales/sales";
    }

    /**
     * Xem chi tiết order của bàn (Order Details Modal)
     */
    @GetMapping("/view-detail")
    public String getViewDetail(@RequestParam Integer tableId, Model model) {
        TableEntity table = tableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bàn với ID: " + tableId));
        ReservationEntity reservation = reservationService.findCurrentReservationByTableId(tableId);

        if (reservation == null) {
            model.addAttribute("tables", tableRepository.findAll());
            model.addAttribute("showOrderDetailModal", false);
            model.addAttribute("orderDetailError", "Không có thông tin đặt bàn/order cho bàn này!");
            return "sales/sales";
        }

        InvoiceEntity invoice = reservation.getInvoice();
        List<InvoiceDetailEntity> invoiceDetails = invoiceDetailRepository.findAllByInvoice_IdAndIsDeletedFalse(invoice.getId());

        OrderDetailRessponse orderDetail = orderDetailMapper.toOrderDetailResponse(table, invoice, reservation, invoiceDetails);

        model.addAttribute("tables", tableRepository.findAll());
        model.addAttribute("orderDetail", orderDetail);
        model.addAttribute("showOrderDetailModal", true);
        return "sales/sales";
    }

    /**
     * Hiển thị form thanh toán (Payment Modal)
     */
    @GetMapping("/show-payment-modal")
    public String showPaymentModal(@RequestParam Integer tableId, Model model) {
        TableEntity table = tableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bàn với ID: " + tableId));
        if (table.getStatus() != TableStatus.OCCUPIED) {
            model.addAttribute("tables", tableRepository.findAll());
            model.addAttribute("errorMessage", "Chỉ có thể thanh toán bàn đang sử dụng (OCCUPIED)!");
            return "sales/sales";
        }

        ReservationEntity reservation = reservationService.findCurrentReservationByTableId(tableId);
        if (reservation == null) {
            model.addAttribute("tables", tableRepository.findAll());
            model.addAttribute("errorMessage", "Không có thông tin đặt bàn để thanh toán!");
            return "sales/sales";
        }

        InvoiceEntity invoice = reservation.getInvoice();
        if (invoice == null) {
            model.addAttribute("tables", tableRepository.findAll());
            model.addAttribute("errorMessage", "Không có hóa đơn để thanh toán!");
            return "sales/sales";
        }
        List<InvoiceDetailEntity> invoiceDetails = invoiceDetailRepository.findAllByInvoice_IdAndIsDeletedFalse(invoice.getId());

        OrderDetailRessponse orderDetail = orderDetailMapper.toOrderDetailResponse(table, invoice, reservation, invoiceDetails);

        model.addAttribute("tables", tableRepository.findAll());
        model.addAttribute("orderDetail", orderDetail);
        model.addAttribute("showPaymentModal", true);
        return "sales/sales";
    }

    /**
     * Xác nhận thanh toán (Pay Invoice)
     */
    @PostMapping("/pay-invoice")
    public String payInvoice(@RequestParam Integer tableId, Model model, RedirectAttributes redirectAttributes) {
        try {
            TableEntity table = tableRepository.findById(tableId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bàn với ID: " + tableId));
            ReservationEntity reservation = reservationService.findCurrentReservationByTableId(tableId);
            if (reservation == null) {
                model.addAttribute("tables", tableRepository.findAll());
                model.addAttribute("errorMessage", "Không có thông tin đặt bàn để thanh toán!");
                return "sales/sales";
            }
            InvoiceEntity invoice = reservation.getInvoice();
            if (invoice == null) {
                model.addAttribute("tables", tableRepository.findAll());
                model.addAttribute("errorMessage", "Không có hóa đơn để thanh toán!");
                return "sales/sales";
            }
            // Cập nhật trạng thái sau thanh toán
            invoice.setStatus(com.viettridao.cafe.common.InvoiceStatus.PAID);
            reservation.setIsDeleted(true);
            table.setStatus(TableStatus.AVAILABLE);
            reservationService.saveReservationAndRelated(reservation, invoice, table);

            redirectAttributes.addFlashAttribute("successMessage", "Thanh toán thành công!");
            return "redirect:/sale";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "Đã xảy ra lỗi hệ thống: " + e.getMessage());
        }
        model.addAttribute("tables", tableRepository.findAll());
        return "sales/sales";
    }

    /**
     * Hiển thị form chọn thực đơn (Select Menu)
     */
    @GetMapping("/show-select-menu-form")
    public String showSelectMenuForm(@RequestParam(value = "tableId", required = false) Integer tableId, Model model) {
        if (tableId == null) {
            model.addAttribute("errorMessage", "Bạn chưa chọn bàn để thực hiện chức năng này!");
            model.addAttribute("tables", tableRepository.findAll());
            return "sales/sales";
        }
        TableEntity table = tableRepository.findById(tableId)
                .orElse(null);
        if (table == null) {
            model.addAttribute("errorMessage", "Không tìm thấy bàn với ID: " + tableId);
            model.addAttribute("tables", tableRepository.findAll());
            return "sales/sales";
        }
        CreateSelectMenuRequest selectMenuRequest = new CreateSelectMenuRequest();
        selectMenuRequest.setTableId(tableId);
        selectMenuRequest.setItems(new ArrayList<>());

        // Pre-fill thông tin khách hàng nếu có reservation
        if (table.getStatus().name().equals("RESERVED") || table.getStatus().name().equals("OCCUPIED")) {
            try {
                ReservationEntity reservation = reservationService.findCurrentReservationByTableId(tableId);
                if (reservation != null) {
                    selectMenuRequest.setCustomerName(reservation.getCustomerName());
                    selectMenuRequest.setCustomerPhone(reservation.getCustomerPhone());
                }
            } catch (Exception e) {
                // Không có reservation hoặc lỗi → để trống để người dùng nhập thủ công
            }
        }

        List<MenuItemResponse> menuItems = selectMenuService.getMenuItems();

        model.addAttribute("tables", tableRepository.findAll());
        model.addAttribute("selectMenuRequest", selectMenuRequest);
        model.addAttribute("selectedTable", table);
        model.addAttribute("showSelectMenuForm", true);
        model.addAttribute("menuItems", menuItems);
        return "sales/sales";
    }

    /**
     * Xử lý chọn thực đơn trên sales
     */
    @PostMapping("/select-menu-on-sales")
    public String selectMenuOnSales(
            @Valid @ModelAttribute("selectMenuRequest") CreateSelectMenuRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        TableEntity table = null;
        try {
            // Validate bàn
            if (request.getTableId() == null) {
                bindingResult.reject("error.tableId", "Không xác định được bàn để chọn món.");
            } else {
                table = tableRepository.findById(request.getTableId()).orElse(null);
                if (table == null) {
                    bindingResult.reject("error.tableId", "Không tìm thấy bàn với ID: " + request.getTableId());
                }
            }
            // Kiểm tra thông tin khách hàng khi bàn AVAILABLE
            if (table != null && table.getStatus().name().equals("AVAILABLE")) {
                if (request.getCustomerName() == null || request.getCustomerName().trim().isEmpty()) {
                    bindingResult.rejectValue("customerName", "error.customerName", "Tên khách hàng không được để trống khi tạo mới order");
                }
                if (request.getCustomerPhone() == null || request.getCustomerPhone().trim().isEmpty()) {
                    bindingResult.rejectValue("customerPhone", "error.customerPhone", "Số điện thoại không được để trống khi tạo mới order");
                }
            }
            // Validate món đã chọn
            if (request.getItems() == null || request.getItems().isEmpty()) {
                bindingResult.reject("error.items", "Vui lòng chọn ít nhất một món");
            } else {
                List<CreateSelectMenuRequest.MenuOrderItem> validItems = request.getItems().stream()
                        .filter(item -> item.getMenuItemId() != null && item.getQuantity() != null && item.getQuantity() > 0)
                        .toList();
                if (validItems.isEmpty()) {
                    bindingResult.reject("error.items", "Vui lòng chọn ít nhất một món và nhập số lượng");
                } else {
                    request.setItems(new ArrayList<>(validItems));
                }
            }
            if (bindingResult.hasErrors()) {
                model.addAttribute("tables", tableRepository.findAll());
                model.addAttribute("selectMenuRequest", request);
                model.addAttribute("selectedTable", table);
                model.addAttribute("showSelectMenuForm", true);
                model.addAttribute("org.springframework.validation.BindingResult.selectMenuRequest", bindingResult);
                model.addAttribute("menuItems", selectMenuService.getMenuItems());
                return "sales/sales";
            }

            // Lấy thông tin nhân viên từ Security Context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            AccountEntity account = accountRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thông tin nhân viên cho tài khoản đăng nhập: " + username));
            if (account.getEmployee() == null) {
                throw new IllegalArgumentException("Tài khoản '" + username + "' không liên kết với nhân viên nào!");
            }
            Integer employeeId = account.getEmployee().getId();

            // Gọi service xử lý nghiệp vụ
            OrderDetailRessponse orderDetail = selectMenuService.createOrderForAvailableTable(request, employeeId);

            model.addAttribute("tables", tableRepository.findAll());
            model.addAttribute("successMessage", "Chọn món thành công!");
            model.addAttribute("orderDetail", orderDetail);
            model.addAttribute("showSelectMenuForm", false);
            return "sales/sales";
        } catch (IllegalArgumentException e) {
            bindingResult.reject("error.system", e.getMessage());
        } catch (RuntimeException e) {
            bindingResult.reject("error.system", "Đã xảy ra lỗi hệ thống: " + e.getMessage());
        }
        model.addAttribute("tables", tableRepository.findAll());
        model.addAttribute("selectMenuRequest", request);
        model.addAttribute("selectedTable", table);
        model.addAttribute("showSelectMenuForm", true);
        model.addAttribute("org.springframework.validation.BindingResult.selectMenuRequest", bindingResult);
        model.addAttribute("menuItems", selectMenuService.getMenuItems());
        return "sales/sales";
    }

    /**
     * Hiển thị form đặt bàn (Reservation)
     */
    @GetMapping("/show-reservation-form")
    public String showReservationForm(@RequestParam Integer tableId, Model model) {
        CreateReservationRequest reservation = new CreateReservationRequest();
        reservation.setTableId(tableId);
        model.addAttribute("tables", tableRepository.findAll());
        model.addAttribute("reservation", reservation);
        model.addAttribute("showReservationForm", true);
        return "sales/sales";
    }

    /**
     * Xử lý tạo reservation (đặt bàn)
     */
    @PostMapping("/reservations")
    public String createReservation(@Valid @ModelAttribute("reservation") CreateReservationRequest request,
                                    BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (bindingResult.hasErrors()) {
                model.addAttribute("tables", tableRepository.findAll());
                model.addAttribute("reservation", request);
                model.addAttribute("showReservationForm", true);
                return "sales/sales";
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            AccountEntity account = accountRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thông tin nhân viên cho tài khoản đăng nhập: " + username));
            if (account.getEmployee() == null) {
                throw new IllegalArgumentException("Tài khoản '" + username + "' không liên kết với nhân viên nào!");
            }
            Integer employeeId = account.getEmployee().getId();

            reservationService.createReservation(request, employeeId);

            redirectAttributes.addFlashAttribute("success", "Đặt bàn thành công!");
            return "redirect:/sale";
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("customerName", "error.customerName", e.getMessage());
        } catch (RuntimeException e) {
            bindingResult.reject("error.system", "Đã xảy ra lỗi hệ thống: " + e.getMessage());
        }
        model.addAttribute("tables", tableRepository.findAll());
        model.addAttribute("reservation", request);
        model.addAttribute("showReservationForm", true);
        return "sales/sales";
    }

    /**
     * Hiển thị form hủy bàn (Cancel Reservation)
     */
    @GetMapping("/show-cancel-reservation-form")
    public String showCancelReservationForm(@RequestParam Integer tableId, Model model) {
        TableEntity table = tableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bàn với ID: " + tableId));
        ReservationEntity reservation = reservationService.findCurrentReservationByTableId(tableId);
        if (reservation == null || Boolean.TRUE.equals(reservation.getIsDeleted())) {
            model.addAttribute("tables", tableRepository.findAll());
            model.addAttribute("errorMessage", "Không tìm thấy thông tin đặt bàn để hủy!");
            return "sales/sales";
        }
        if (!"RESERVED".equals(reservation.getTable().getStatus().name())) {
            model.addAttribute("tables", tableRepository.findAll());
            model.addAttribute("errorMessage", "Chỉ có thể hủy bàn ở trạng thái ĐÃ ĐẶT (RESERVED)!");
            return "sales/sales";
        }
        model.addAttribute("tables", tableRepository.findAll());
        model.addAttribute("selectedTable", table);
        model.addAttribute("reservation", reservation);
        model.addAttribute("showCancelReservationForm", true);
        return "sales/sales";
    }

    /**
     * Xử lý hủy bàn (Cancel Reservation)
     */
    @PostMapping("/cancel-reservation")
    public String cancelReservation(@RequestParam Integer tableId, Model model) {
        ReservationEntity reservation = reservationService.findCurrentReservationByTableId(tableId);
        if (reservation == null || Boolean.TRUE.equals(reservation.getIsDeleted())) {
            model.addAttribute("tables", tableRepository.findAll());
            model.addAttribute("errorMessage", "Không tìm thấy thông tin đặt bàn để hủy!");
            model.addAttribute("hideCancelModal", true);
            return "sales/sales";
        }
        if (!"RESERVED".equals(reservation.getTable().getStatus().name())) {
            model.addAttribute("tables", tableRepository.findAll());
            model.addAttribute("errorMessage", "Chỉ có thể hủy bàn ở trạng thái ĐÃ ĐẶT!");
            model.addAttribute("hideCancelModal", true);
            return "sales/sales";
        }
        reservation.setIsDeleted(true);
        InvoiceEntity invoice = reservation.getInvoice();
        if (invoice != null) {
            invoice.setIsDeleted(true);
            if (invoice.getId() != null) {
                List<InvoiceDetailEntity> invoiceDetails = invoiceDetailRepository.findAllByInvoice_IdAndIsDeletedFalse(invoice.getId());
                for (InvoiceDetailEntity detail : invoiceDetails) {
                    detail.setIsDeleted(true);
                }
                invoiceDetailRepository.saveAll(invoiceDetails);
            }
        }
        TableEntity table = reservation.getTable();
        table.setStatus(TableStatus.AVAILABLE);
        reservationService.saveReservationAndRelated(reservation, invoice, table);
        model.addAttribute("tables", tableRepository.findAll());
        model.addAttribute("successMessage", "Hủy bàn thành công!");
        model.addAttribute("hideCancelModal", true);
        return "sales/sales";
    }

    /**
     * Hiển thị form chuyển bàn (Move Table)
     */
    @GetMapping("/show-move-table-form")
    public String showMoveTableForm(@RequestParam Integer selectedTableId, Model model) {
        try {
            Optional<TableEntity> sourceTableOpt = tableRepository.findById(selectedTableId);
            if (sourceTableOpt.isEmpty()) {
                model.addAttribute("errorMessage", "Không tìm thấy bàn nguồn với ID: " + selectedTableId);
                return "sales/sales";
            }
            TableEntity sourceTable = sourceTableOpt.get();
            if (sourceTable.getStatus() != TableStatus.OCCUPIED) {
                model.addAttribute("errorMessage", "Chỉ có thể chuyển từ bàn đang sử dụng (OCCUPIED). Bàn hiện tại: " + sourceTable.getStatus());
                return "sales/sales";
            }
            ReservationEntity sourceReservation = reservationService.findCurrentReservationByTableId(selectedTableId);
            if (sourceReservation == null) {
                model.addAttribute("errorMessage", "Không tìm thấy thông tin đặt bàn cho bàn nguồn");
                return "sales/sales";
            }
            List<TableEntity> allTables = tableRepository.findAll();
            List<TableEntity> availableTables = allTables.stream().filter(table -> table.getStatus() == TableStatus.AVAILABLE).toList();
            if (availableTables.isEmpty()) {
                model.addAttribute("errorMessage", "Không có bàn trống nào để chuyển đến");
                return "sales/sales";
            }
            model.addAttribute("showMoveModal", true);
            model.addAttribute("selectedTableId", selectedTableId);
            model.addAttribute("tables", allTables);
            model.addAttribute("sourceTable", sourceTable);
            model.addAttribute("availableTables", availableTables);
            model.addAttribute("selectMenuRequest", new CreateSelectMenuRequest());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi khi thiết lập form chuyển bàn: " + e.getMessage());
            return "sales/sales";
        }
        return "sales/sales";
    }

    /**
     * Xử lý chuyển bàn (Move Table)
     */
    @PostMapping("/move-table")
    public String moveTable(
            @RequestParam(required = false) Integer sourceTableId,
            @RequestParam(required = false) Integer targetTableId,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            if (sourceTableId == null) {
                model.addAttribute("errorMessage", "Không tìm thấy bàn nguồn");
                model.addAttribute("tables", tableRepository.findAll());
                model.addAttribute("showMoveModal", true);
                model.addAttribute("selectedTableId", sourceTableId);
                model.addAttribute("reservation", new CreateReservationRequest());
                model.addAttribute("selectMenuRequest", new CreateSelectMenuRequest());
                return "sales/sales";
            }
            if (targetTableId == null) {
                model.addAttribute("errorMessage", "Vui lòng chọn bàn đích");
                model.addAttribute("tables", tableRepository.findAll());
                model.addAttribute("showMoveModal", true);
                model.addAttribute("selectedTableId", sourceTableId);
                model.addAttribute("reservation", new CreateReservationRequest());
                model.addAttribute("selectMenuRequest", new CreateSelectMenuRequest());
                return "sales/sales";
            }
            com.viettridao.cafe.dto.request.sales.MoveTableRequest request = new com.viettridao.cafe.dto.request.sales.MoveTableRequest();
            request.setSourceTableId(sourceTableId);
            request.setTargetTableId(targetTableId);
            Integer employeeId = getCurrentEmployeeId();
            reservationService.moveTable(request, employeeId);

            redirectAttributes.addFlashAttribute("successMessage", "Chuyển bàn thành công!");
            return "redirect:/sale";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "Đã xảy ra lỗi hệ thống: " + e.getMessage());
        }
        model.addAttribute("tables", tableRepository.findAll());
        model.addAttribute("showMoveModal", true);
        model.addAttribute("selectedTableId", sourceTableId);
        model.addAttribute("reservation", new CreateReservationRequest());
        model.addAttribute("selectMenuRequest", new CreateSelectMenuRequest());
        return "sales/sales";
    }

    /**
     * Hiển thị form gộp bàn (Merge Table)
     */
    @GetMapping("/show-merge-table-form")
    public String showMergeTableForm(@RequestParam Integer selectedTableId, Model model) {
        List<TableEntity> occupiedTables = tableRepository.findAll().stream()
                .filter(table -> table.getStatus() == TableStatus.OCCUPIED)
                .toList();

        model.addAttribute("showMergeModal", true);
        model.addAttribute("occupiedTables", occupiedTables);
        model.addAttribute("selectedTableId", selectedTableId);

        return "sales/sales";
    }

    /**
     * Xử lý gộp bàn (Merge Tables)
     */
    @PostMapping("/merge-tables")
    public String mergeTables(@Valid @ModelAttribute("mergeTableRequest") MergeTableRequest request,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        try {
            if (bindingResult.hasErrors()) {
                List<TableEntity> occupiedTables = tableRepository.findAll().stream()
                        .filter(table -> table.getStatus() == TableStatus.OCCUPIED)
                        .toList();
                model.addAttribute("tables", tableRepository.findAll());
                model.addAttribute("mergeTableRequest", request);
                model.addAttribute("showMergeModal", true);
                model.addAttribute("occupiedTables", occupiedTables);
                model.addAttribute("org.springframework.validation.BindingResult.mergeTableRequest", bindingResult);
                return "sales/sales";
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            AccountEntity account = accountRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thông tin nhân viên cho tài khoản đăng nhập!"));
            if (account.getEmployee() == null) {
                throw new IllegalArgumentException("Không tìm thấy thông tin nhân viên cho tài khoản đăng nhập!");
            }
            Integer employeeId = account.getEmployee().getId();
            reservationService.mergeTables(request, employeeId);
            redirectAttributes.addFlashAttribute("successMessage", "Gộp bàn thành công!");
            return "redirect:/sale";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "Đã xảy ra lỗi hệ thống: " + e.getMessage());
        }
        List<TableEntity> occupiedTables = tableRepository.findAll().stream()
                .filter(table -> table.getStatus() == TableStatus.OCCUPIED)
                .toList();
        model.addAttribute("tables", tableRepository.findAll());
        model.addAttribute("mergeTableRequest", request);
        model.addAttribute("showMergeModal", true);
        model.addAttribute("occupiedTables", occupiedTables);
        model.addAttribute("org.springframework.validation.BindingResult.mergeTableRequest", bindingResult);
        return "sales/sales";
    }

    /**
     * Hiển thị form tách bàn (Split Table)
     */
    @GetMapping("/show-split-table-form")
    public String showSplitTableForm(@RequestParam Integer selectedTableId, Model model) {
        try {
            Optional<TableEntity> sourceTableOpt = tableRepository.findById(selectedTableId);
            if (sourceTableOpt.isEmpty()) {
                model.addAttribute("errorMessage", "Không tìm thấy bàn nguồn với ID: " + selectedTableId);
                return "sales/sales";
            }
            TableEntity sourceTable = sourceTableOpt.get();
            if (sourceTable.getStatus() != TableStatus.OCCUPIED) {
                model.addAttribute("errorMessage", "Chỉ có thể tách từ bàn đang sử dụng (OCCUPIED). Bàn hiện tại: " + sourceTable.getStatus());
                return "sales/sales";
            }
            ReservationEntity sourceReservation = reservationService.findCurrentReservationByTableId(selectedTableId);
            if (sourceReservation == null) {
                model.addAttribute("errorMessage", "Không tìm thấy thông tin đặt bàn cho bàn nguồn");
                return "sales/sales";
            }
            if (sourceReservation.getInvoice() == null) {
                model.addAttribute("errorMessage", "Không tìm thấy hóa đơn cho bàn nguồn");
                return "sales/sales";
            }
            List<InvoiceDetailEntity> invoiceDetails = invoiceDetailRepository.findAllByInvoice_IdAndIsDeletedFalse(sourceReservation.getInvoice().getId());
            if (invoiceDetails.isEmpty()) {
                model.addAttribute("errorMessage", "Bàn nguồn không có món nào để tách");
                return "sales/sales";
            }
            List<TableEntity> allTables = tableRepository.findAll();
            List<TableEntity> availableTables = allTables.stream()
                    .filter(table -> table.getStatus() == TableStatus.AVAILABLE)
                    .toList();
            List<TableEntity> occupiedTables = allTables.stream().filter(table -> table.getStatus() == TableStatus.OCCUPIED && !table.getId().equals(selectedTableId)).toList();
            if (availableTables.isEmpty() && occupiedTables.isEmpty()) {
                model.addAttribute("errorMessage", "Không có bàn nào khả dụng để tách đến");
                return "sales/sales";
            }
            SplitTableRequest splitRequest = new SplitTableRequest();
            splitRequest.setSourceTableId(selectedTableId);
            List<SplitTableRequest.SplitItemRequest> items = new ArrayList<>();
            for (InvoiceDetailEntity detail : invoiceDetails) {
                SplitTableRequest.SplitItemRequest item = new SplitTableRequest.SplitItemRequest();
                item.setMenuItemId(detail.getMenuItem().getId());
                item.setQuantity(0);
                items.add(item);
            }
            splitRequest.setItems(items);

            model.addAttribute("showSplitModal", true);
            model.addAttribute("sourceTable", sourceTable);
            model.addAttribute("availableTables", availableTables);
            model.addAttribute("occupiedTables", occupiedTables);
            model.addAttribute("sourceInvoiceDetails", invoiceDetails);
            model.addAttribute("selectedTableId", selectedTableId);
            model.addAttribute("splitTableRequest", splitRequest);

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi khi thiết lập form tách bàn: " + e.getMessage());
            return "sales/sales";
        }
        return "sales/sales";
    }

    /**
     * Xử lý tách bàn (Split Table)
     */
    @PostMapping("/split-table")
    public String splitTable(@Valid @ModelAttribute SplitTableRequest request,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        try {
            if (bindingResult.hasErrors()) {
                return setupSplitModalOnError(request, model, bindingResult, "Lỗi validation dữ liệu đầu vào");
            }
            if (request.getItems() == null || request.getItems().isEmpty()) {
                return setupSplitModalOnError(request, model, bindingResult, "Vui lòng chọn ít nhất một món để tách");
            }
            List<SplitTableRequest.SplitItemRequest> validItems = request.getItems().stream()
                    .filter(item -> item.getMenuItemId() != null && item.getQuantity() != null && item.getQuantity() > 0)
                    .toList();

            if (validItems.isEmpty()) {
                return setupSplitModalOnError(request, model, bindingResult, "Vui lòng nhập số lượng hợp lệ cho các món được chọn");
            }
            request.setItems(new ArrayList<>(validItems));
            Integer employeeId = getCurrentEmployeeId();

            reservationService.splitTable(request, employeeId);

            redirectAttributes.addFlashAttribute("successMessage", "Tách bàn thành công! Đã chuyển " + validItems.size() + " món từ bàn nguồn sang bàn đích.");
            return "redirect:/sale";

        } catch (IllegalArgumentException e) {
            return setupSplitModalOnError(request, model, bindingResult, e.getMessage());

        } catch (RuntimeException e) {
            return setupSplitModalOnError(request, model, bindingResult, "Đã xảy ra lỗi hệ thống: " + e.getMessage());
        }
    }

    /**
     * Helper: Lấy ID nhân viên hiện tại từ session đăng nhập
     */
    private Integer getCurrentEmployeeId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        AccountEntity account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thông tin tài khoản cho username: " + username));
        if (account.getEmployee() == null) {
            throw new IllegalArgumentException("Tài khoản '" + username + "' không liên kết với nhân viên nào!");
        }
        return account.getEmployee().getId();
    }

    /**
     * Helper: Thiết lập lại modal tách bàn khi có lỗi (setup lại dữ liệu cho view)
     */
    private String setupSplitModalOnError(SplitTableRequest request, Model model,
                                          BindingResult bindingResult, String errorMessage) {
        try {
            List<TableEntity> allTables = tableRepository.findAll();
            List<TableEntity> availableTables = allTables.stream().filter(table -> table.getStatus() == TableStatus.AVAILABLE).toList();
            List<TableEntity> occupiedTables = allTables.stream().filter(table -> table.getStatus() == TableStatus.OCCUPIED && !table.getId().equals(request.getSourceTableId())).toList();
            TableEntity sourceTable = tableRepository.findById(request.getSourceTableId()).orElse(null);
            ReservationEntity sourceReservation = reservationService.findCurrentReservationByTableId(request.getSourceTableId());
            List<com.viettridao.cafe.model.InvoiceDetailEntity> invoiceDetails = new ArrayList<>();
            if (sourceReservation != null && sourceReservation.getInvoice() != null) {
                invoiceDetails = invoiceDetailRepository.findAllByInvoice_IdAndIsDeletedFalse(sourceReservation.getInvoice().getId());
            }
            model.addAttribute("tables", allTables);
            model.addAttribute("splitTableRequest", request);
            model.addAttribute("showSplitModal", true);
            model.addAttribute("sourceTable", sourceTable);
            model.addAttribute("availableTables", availableTables);
            model.addAttribute("occupiedTables", occupiedTables);
            model.addAttribute("sourceInvoiceDetails", invoiceDetails);
            model.addAttribute("selectedTableId", request.getSourceTableId());
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("org.springframework.validation.BindingResult.splitTableRequest", bindingResult);

            return "sales/sales";

        } catch (Exception e) {
            model.addAttribute("tables", tableRepository.findAll());
            model.addAttribute("errorMessage", "Lỗi hệ thống khi hiển thị form: " + e.getMessage());
            return "sales/sales";
        }
    }

}