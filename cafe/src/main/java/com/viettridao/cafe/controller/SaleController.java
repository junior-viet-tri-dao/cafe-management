package com.viettridao.cafe.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.dto.request.invoice.InvoiceRequest;
import com.viettridao.cafe.dto.request.reservation.ReservationCreateRequest;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.service.invoice.IInvoiceService;
import com.viettridao.cafe.service.invoice_detail.IInvoiceDetailService;
import com.viettridao.cafe.service.menuItem.IMenuItemService;
import com.viettridao.cafe.service.pdf.IPdfExportService;
import com.viettridao.cafe.service.reservation.IReservationService;
import com.viettridao.cafe.service.table.ITableService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sale")
public class SaleController {
    private final ITableService tableService;
    private final IReservationService reservationService;
    private final IInvoiceService invoiceService;
    private final IMenuItemService menuItemService;
    private final IInvoiceDetailService invoiceDetailService;
    private final IPdfExportService pdfExportService;

    @GetMapping
    public String listTable(Model model) {
        model.addAttribute("tables", tableService.getAllTables());
        return "sale/table_list";
    }

    @GetMapping("reservation/new")
    public String showReservationForm(@RequestParam(name = "tableIds", required = false) List<Integer> tableIds, Model model) {
        if (tableIds == null || tableIds.isEmpty()) {
            return "redirect:/sale";
        }
        ReservationCreateRequest reservation = new ReservationCreateRequest();
        reservation.setTableId(tableIds.get(0));
        model.addAttribute("reservation", reservation);

        return "sale/reservation_form";
    }

    @PostMapping("reservation")
    public String createReservation(@ModelAttribute ReservationCreateRequest request) {
        reservationService.createReservation(request);
        return "redirect:/sale";
    }

    @PostMapping("cancel")
    public String cancelReservation(@RequestParam("tableIds") List<Integer> tableIds, Model model) {
        for (Integer tableId : tableIds) {
            invoiceService.findByTableId(tableId).ifPresent(invoice -> {
                boolean canCancel =
                        invoice.getTotalAmount() == 0.0 &&
                                !invoice.getDeleted() &&
                                (invoice.getStatus() == InvoiceStatus.PENDING_PAYMENT || invoice.getStatus() == InvoiceStatus.RESERVED);

                if (canCancel) {
                    reservationService.deleteReservation(invoice.getId());
                    System.out.println("✅ Hủy bàn " + tableId + " thành công");
                } else {
                    System.out.println("❌ Không thể hủy bàn " + tableId
                            + " | Tổng tiền: " + invoice.getTotalAmount()
                            + " | Trạng thái: " + invoice.getStatus()
                            + " | Deleted: " + invoice.getDeleted());
                }
            });
        }
        return "redirect:/sale";
    }

    @GetMapping("/view_detail")
    public String viewTableDetail(@RequestParam("tableIds") List<Integer> tableIds, Model model) {
        if (tableIds == null || tableIds.isEmpty()) {
            return "redirect:/sale";
        }
        Integer tableId = tableIds.get(0);

        model.addAttribute("table", tableService.getTableById(tableId));

        return invoiceService.findByTableId(tableId)
                .filter(invoice -> invoice.getStatus() == InvoiceStatus.PENDING_PAYMENT
                        || invoice.getStatus() == InvoiceStatus.RESERVED)
                .map(invoice -> {
                    model.addAttribute("invoice", invoice);
                    if (!invoice.getReservations().isEmpty()) {
                        model.addAttribute("reservation", invoice.getReservations().get(0));
                    }
                    return "sale/table_detail";
                })
                .orElseGet(() -> {
                    model.addAttribute("errorMessage", "Bàn này chưa có hóa đơn hợp lệ để hiển thị chi tiết!");
                    return "sale/table_detail";
                });
    }

    @GetMapping("/select")
    public String showSelectMenuForm(@RequestParam("tableId") Integer tableId, Model model) {
        model.addAttribute("menuItems", menuItemService.getMenuItemAll());
        model.addAttribute("table", tableService.getTableById(tableId));

        InvoiceRequest invoiceRequest = new InvoiceRequest();
        invoiceRequest.setTableId(tableId);
        model.addAttribute("invoiceRequest", invoiceRequest);

        return "sale/select_menu";
    }

    @PostMapping("/select")
    public String processMenuSelection(@ModelAttribute InvoiceRequest invoiceRequest) {
        invoiceDetailService.createMenusForInvoice(invoiceRequest);
        return "redirect:/sale/view_detail?tableIds=" + invoiceRequest.getTableId();
    }

    @GetMapping("/payment")
    public String processPaymentForm(@RequestParam("tableId") Integer tableId, Model model) {
        return invoiceService.findByTableId(tableId)
                .filter(invoice -> invoice.getStatus() == InvoiceStatus.PENDING_PAYMENT)
                .map(invoice -> {
                    model.addAttribute("invoice", invoice);
                    model.addAttribute("table", tableService.getTableById(tableId));
                    model.addAttribute("amountPaid", 0.0);
                    model.addAttribute("change", 0.0);
                    return "sale/payment_form";
                })
                .orElseGet(() -> {
                    model.addAttribute("errorMessage", "Không tìm thấy hóa đơn hợp lệ để thanh toán.");
                    return "sale/table_list";
                });
    }

    @PostMapping("/payment")
    public String processPaymentForm(@RequestParam("tableId") Integer tableId,
                                     @RequestParam("amountPaid") Double amountPaid,
                                     Model model) {
        InvoiceEntity invoice = invoiceService.findByTableId(tableId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        if (amountPaid < invoice.getTotalAmount()) {
            model.addAttribute("invoice", invoice);
            model.addAttribute("table", tableService.getTableById(tableId));
            model.addAttribute("amountPaid", amountPaid);
            model.addAttribute("change", 0.0);
            model.addAttribute("errorMessage", "Số tiền khách đưa không đủ để thanh toán.");
            return "sale/payment_form";
        }

        // Thanh toán
        invoiceService.payment(tableId);

        Double change = amountPaid - invoice.getTotalAmount();

        model.addAttribute("invoice", invoice);
        model.addAttribute("table", tableService.getTableById(tableId));
        model.addAttribute("amountPaid", amountPaid);
        model.addAttribute("change", change);
        model.addAttribute("successMessage", "Thanh toán thành công!");

        return "sale/payment_form";
    }

    @GetMapping("/payment/pdf")
    public ResponseEntity<byte[]> downloadInvoicePdf(@RequestParam("tableId") Integer tableId) {
        InvoiceEntity invoice = invoiceService.findByTableId(tableId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn"));

        byte[] pdfBytes = pdfExportService.exportInvoiceToPdf(invoice);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("HoaDon_Ban_" + tableId + ".pdf")
                .build());

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/switch")
    public String showSwitchTableForm(@RequestParam("sourceTableId") Integer sourceTableId, Model model) {
        model.addAttribute("currentTableId", sourceTableId);
        model.addAttribute("tables", tableService.getAllTables());
        return "sale/switch_table_form";
    }

    @PostMapping("/switch")
    public String processSwitchTable(@RequestParam("sourceTableId") Integer sourceTableId,
                                     @RequestParam("targetTableId") Integer targetTableId,
                                     RedirectAttributes redirectAttributes) {
        try {
            tableService.switchTable(sourceTableId, targetTableId);
            redirectAttributes.addFlashAttribute("successMessage", "Chuyển bàn thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/sale";
    }


    @GetMapping("/merge")
    public String showMergeTableForm(Model model) {
        model.addAttribute("tables", tableService.getAllTables());
        return "sale/merge_table_form";
    }


    @PostMapping("/merge")
    public String processMergeTable(@RequestParam("targetTableId") Integer targetTableId,
                                    @RequestParam("sourceTableIds") List<Integer> sourceTableIds,
                                    Model model) {
        try {
            tableService.mergeTables(sourceTableIds, targetTableId);
            model.addAttribute("successMessage", "Gộp bàn thành công!");
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/sale";
    }

    @GetMapping("/split")
    public String showSplitTableForm(@RequestParam("sourceTableId") Integer sourceTableId,
                                     @RequestParam(name = "targetTableId", required = false) Integer targetTableId,
                                     Model model) {

        InvoiceEntity invoice = invoiceService.findByTableId(sourceTableId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn cho bàn này"));

        model.addAttribute("sourceInvoice", invoice);
        model.addAttribute("sourceTable", tableService.getTableById(sourceTableId));
        if (targetTableId != null) {
            model.addAttribute("targetTableId", targetTableId);
            model.addAttribute("targetTableName", tableService.getTableById(targetTableId).getTableName());

            invoiceService.findByTableId(targetTableId).ifPresent(targetInvoice -> {
                model.addAttribute("targetInvoice", targetInvoice);
            });
        }

        model.addAttribute("allMenuItems", menuItemService.getMenuItemAll());

        return "sale/split_table_form";
    }


    @PostMapping("/split")
    public String processSplitTable(@RequestParam("sourceTableId") Integer sourceTableId,
                                    @RequestParam("targetTableId") Integer targetTableId,
                                    @RequestParam Map<String, String> allParams,
                                    Model model) {
        try {
            // Parse quantity cho từng món (menuItemId -> quantity)
            Map<Integer, Integer> menuItemIdToQuantity = new HashMap<>();
            for (Map.Entry<String, String> entry : allParams.entrySet()) {
                if (entry.getKey().startsWith("item_")) {
                    Integer menuItemId = Integer.parseInt(entry.getKey().replace("item_", ""));
                    Integer quantity = Integer.parseInt(entry.getValue());
                    if (quantity > 0) {
                        menuItemIdToQuantity.put(menuItemId, quantity);
                    }
                }
            }

            tableService.splitTable(sourceTableId, targetTableId, menuItemIdToQuantity);
            model.addAttribute("successMessage", "Tách bàn thành công!");
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/sale";
    }

    @PostMapping("/split-item")
    public String splitItemManually(@RequestParam("itemId") Integer itemId,
                                    @RequestParam("sourceTableId") Integer sourceTableId,
                                    @RequestParam("targetTableId") Integer targetTableId,
                                    @RequestParam("quantity") Integer quantity,
                                    RedirectAttributes redirectAttributes) {
        try {
            Map<Integer, Integer> itemMap = new HashMap<>();
            itemMap.put(itemId, quantity);

            tableService.splitTable(sourceTableId, targetTableId, itemMap);

            redirectAttributes.addFlashAttribute("successMessage", "Chuyển món thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/sale/split?sourceTableId=" + sourceTableId + "&targetTableId=" + targetTableId;
    }



}
