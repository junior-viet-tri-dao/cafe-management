
package com.viettridao.cafe.controller;

import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.service.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tables")
public class SaleController {
    // In hóa đơn cho order/bàn
    @GetMapping("/invoice/{tableId}")
    public String printInvoice(@PathVariable Integer tableId, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Lấy order chưa thanh toán hoặc order gần nhất của bàn
            var orderOpt = paymentService.findUnpaidOrderByTableId(tableId);
            if (orderOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy order để in hóa đơn!");
                return "redirect:/tables";
            }
            var order = orderOpt.get();
            // Lấy thông tin chi tiết order, payment, table
            model.addAttribute("order", order);
            model.addAttribute("orderDetails", order.getOrderDetails());
            model.addAttribute("table", order.getTable());
            model.addAttribute("total", paymentService.calculateOrderTotal(order.getId()));
            // Nếu có payment, lấy payment gần nhất
            // (Có thể bổ sung lấy payment thực tế nếu cần)
            return "sales/invoice";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi in hóa đơn: " + e.getMessage());
            return "redirect:/tables";
        }
    }

    // Xuất file hóa đơn PDF (giả lập)
    @GetMapping("/invoice/export/{tableId}")
    public void exportInvoicePdf(@PathVariable Integer tableId, HttpServletResponse response)
            throws java.io.IOException {
        // TODO: Lấy dữ liệu order, sinh file PDF thực tế
        response.setHeader("Content-Disposition", "attachment; filename=invoice-" + tableId + ".pdf");
        response.setContentType("application/pdf");
        response.getOutputStream().write("PDF INVOICE DEMO".getBytes());
        response.getOutputStream().flush();
    }

    private final TableService tableService;
    private final com.viettridao.cafe.service.PaymentService paymentService;
    private final com.viettridao.cafe.service.OrderService orderService;
    private final com.viettridao.cafe.repository.OrderDetailRepository orderDetailRepository;
    private final com.viettridao.cafe.service.ReservationService reservationService;
    private final com.viettridao.cafe.repository.ReservationRepository reservationRepository;

    // ...existing code...
    // Đặt bàn trước
    @GetMapping("/reserve/{tableId}")
    public String showReservationForm(@PathVariable Integer tableId, Model model) {
        model.addAttribute("tableId", tableId);
        return "sales/reserve_table";
    }

    @PostMapping("/reserve")
    public String reserveTable(
            @RequestParam Integer tableId,
            @RequestParam String customerName,
            @RequestParam String customerPhone,
            @RequestParam String reservationDate, // yyyy-MM-dd
            RedirectAttributes redirectAttributes) {
        try {
            // Kiểm tra bàn đã có người đặt chưa
            var reservations = reservationRepository.findAllByTable_IdAndIsDeletedFalse(tableId);
            if (reservations != null && !reservations.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Bàn đã có người đặt trước!");
                return "redirect:/tables";
            }
            com.viettridao.cafe.model.ReservationEntity reservation = new com.viettridao.cafe.model.ReservationEntity();
            com.viettridao.cafe.model.ReservationKey key = new com.viettridao.cafe.model.ReservationKey();
            key.setIdTable(tableId);
            // Đơn giản hóa: employeeId, invoiceId = null (hoặc có thể lấy từ session nếu
            // cần)
            reservation.setId(key);
            com.viettridao.cafe.model.TableEntity table = new com.viettridao.cafe.model.TableEntity();
            table.setId(tableId);
            reservation.setTable(table);
            reservation.setCustomerName(customerName);
            reservation.setCustomerPhone(customerPhone);
            reservation.setReservationDate(java.time.LocalDate.parse(reservationDate));
            reservation.setIsDeleted(false);
            reservationService.createReservation(reservation);
            redirectAttributes.addFlashAttribute("success", "Đặt bàn thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi đặt bàn: " + e.getMessage());
        }
        return "redirect:/tables";
    }

    @GetMapping("")
    public String listTables(Model model) {
        List<TableEntity> tables = tableService.getAllTables();
        model.addAttribute("tables", tables);
        return "tables/tables";
    }

    // Đặt món cho bàn
    @GetMapping("/order/{tableId}")
    public String showOrderForm(@PathVariable Integer tableId, Model model) {
        // TODO: Lấy danh sách menuItem từ service (giả sử đã có MenuItemService)
        // List<MenuItemEntity> menuItems = menuItemService.getAllMenuItems();
        // model.addAttribute("menuItems", menuItems);
        model.addAttribute("tableId", tableId);
        return "sales/order_table";
    }

    @PostMapping("/order")
    public String orderTable(
            @RequestParam Integer tableId,
            @RequestParam(required = false, name = "selectedItems") List<Integer> selectedItems,
            @RequestParam(required = false) java.util.Map<String, String> quantities,
            RedirectAttributes redirectAttributes) {
        try {
            if (selectedItems == null || selectedItems.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Chưa chọn món!");
                return "redirect:/tables";
            }
            // Tạo order mới
            com.viettridao.cafe.model.OrderEntity order = new com.viettridao.cafe.model.OrderEntity();
            com.viettridao.cafe.model.TableEntity table = new com.viettridao.cafe.model.TableEntity();
            table.setId(tableId);
            order.setTable(table);
            order.setIsDeleted(false);
            // Lưu order để lấy id
            order = orderService.createOrUpdateOrder(order);
            // Lưu chi tiết order
            for (Integer menuItemId : selectedItems) {
                int quantity = 1;
                if (quantities != null && quantities.get(menuItemId.toString()) != null) {
                    try {
                        quantity = Integer.parseInt(quantities.get(menuItemId.toString()));
                    } catch (Exception ignored) {
                    }
                }
                com.viettridao.cafe.model.OrderDetailEntity detail = new com.viettridao.cafe.model.OrderDetailEntity();
                detail.setOrder(order);
                com.viettridao.cafe.model.MenuItemEntity menuItem = new com.viettridao.cafe.model.MenuItemEntity();
                menuItem.setId(menuItemId);
                detail.setMenuItem(menuItem);
                detail.setQuantity(quantity);
                orderDetailRepository.save(detail);
            }
            redirectAttributes.addFlashAttribute("success", "Lưu order thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi lưu order: " + e.getMessage());
        }
        return "redirect:/tables";
    }
    // ...existing code...

    // Thanh toán bàn
    @GetMapping("/payment/{tableId}")
    public String showPaymentForm(@PathVariable Integer tableId, Model model) {
        // TODO: Lấy danh sách orderItems, tổng tiền từ service
        // model.addAttribute("orderItems", ...);
        // model.addAttribute("total", ...);
        model.addAttribute("tableId", tableId);
        return "sales/payment_table";
    }

    @PostMapping("/payment")
    public String paymentTable(
            @RequestParam Integer tableId,
            @RequestParam Long customerPaid,
            @RequestParam(required = false) String note,
            RedirectAttributes redirectAttributes) {
        try {
            // 1. Lấy order chưa thanh toán của bàn
            var orderOpt = paymentService.findUnpaidOrderByTableId(tableId);
            if (orderOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy order chưa thanh toán cho bàn này!");
                return "redirect:/tables";
            }
            var order = orderOpt.get();
            // 2. Tính tổng tiền
            long total = paymentService.calculateOrderTotal(order.getId());
            if (customerPaid < total) {
                redirectAttributes.addFlashAttribute("error", "Số tiền khách trả không đủ!");
                return "redirect:/tables";
            }
            // 3. Lưu PaymentEntity
            paymentService.processPayment(order, customerPaid, note);
            // 4. Cập nhật trạng thái order và bàn
            paymentService.updateOrderAndTableStatusAfterPayment(order, tableId);
            redirectAttributes.addFlashAttribute("success", "Thanh toán thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi thanh toán: " + e.getMessage());
        }
        return "redirect:/tables";
    }
    // ...existing code...
    // ...existing code...
}
