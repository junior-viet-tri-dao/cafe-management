package com.viettridao.cafe.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.itextpdf.layout.element.Tab;
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
 * ================================================================================================
 * SALES CONTROLLER - QUẢN LÝ BÁN HÀNG VÀ CÁC CHỨC NĂNG BÀN
 * ================================================================================================
 * 
 * Controller này xử lý tất cả các chức năng liên quan đến quản lý bàn và bán
 * hàng:
 * 
 * 📋 DANH SÁCH CHỨC NĂNG CHÍNH:
 * ├── 1. Hiển thị danh sách bàn và trạng thái
 * ├── 2. Đặt bàn (reservation) - chỉ áp dụng cho bàn AVAILABLE
 * ├── 3. Chọn thực đơn - áp dụng cho bàn AVAILABLE, RESERVED, OCCUPIED
 * ├── 4. Xem chi tiết bàn - hiển thị thông tin order và invoice
 * ├── 5. Hủy bàn - chỉ áp dụng cho bàn RESERVED
 * ├── 6. Gộp bàn - gộp nhiều bàn OCCUPIED thành 1 bàn
 * └── 7. Tách bàn - tách một phần món từ bàn OCCUPIED sang bàn khác
 * 
 * 🔄 WORKFLOW TRẠNG THÁI BÀN:
 * AVAILABLE → RESERVED (đặt bàn) → OCCUPIED (chọn món) → AVAILABLE (thanh toán)
 * 
 * 📝 QUY TẮC NGHIỆP VỤ:
 * - Mỗi bàn chỉ có 1 reservation active tại 1 thời điểm
 * - Mỗi reservation tương ứng với 1 invoice
 * - Một invoice có nhiều invoice details (chi tiết món)
 * - Chỉ có thể gộp/tách bàn khi bàn đang OCCUPIED
 * 
 * ⚡ PERFORMANCE NOTES:
 * - Sử dụng @Transactional trong service layer để đảm bảo data consistency
 * - Validate cả frontend (JavaScript) và backend (Spring Validation)
 * - Error handling toàn diện với user-friendly messages
 * 
 * ================================================================================================
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/sale")
public class SalesController {

    // ================================================================================================
    // DEPENDENCY INJECTION - CÁC SERVICE VÀ REPOSITORY CẦN THIẾT
    // ================================================================================================

    /** Repository quản lý thông tin bàn và trạng thái */
    private final TableRepository tableRepository;

    /** Service xử lý logic đặt bàn, gộp bàn, tách bàn */
    private final ReservationService reservationService;

    /** Repository quản lý thông tin tài khoản và nhân viên */
    private final AccountRepository accountRepository;

    /** Service xử lý logic chọn thực đơn và tạo order */
    private final SelectMenuService selectMenuService;

    /** Repository quản lý chi tiết hóa đơn (invoice details) */
    private final InvoiceDetailRepository invoiceDetailRepository;

    /** Mapper chuyển đổi entity sang DTO response */
    private final OrderDetailMapper orderDetailMapper;

    // ================================================================================================
    // CHỨC NĂNG 1: HIỂN THỊ TRANG CHÍNH SALES OVERVIEW
    // ================================================================================================

    @GetMapping("")
    public String getSalesOverview(
            @RequestParam(required = false) Boolean showMergeModal,
            @RequestParam(required = false) Boolean showSplitModal,
            @RequestParam(required = false) Boolean showMoveModal,
            @RequestParam(required = false) Integer selectedTableId,
            Model model) {

        // ======================================
        // BƯỚC 1: CHUẨN BỊ DATA CƠ BẢN CHO VIEW
        // ======================================
        model.addAttribute("tables", tableRepository.findAll()); // Danh sách tất cả bàn
        model.addAttribute("reservation", new CreateReservationRequest()); // Object cho form đặt bàn
        model.addAttribute("showReservationForm", false); // Mặc định không hiển thị form đặt bàn


        // ======================================
        // BƯỚC 2: THÊM CÁC OBJECT MẶC ĐỊNH CHO TEMPLATE
        // ======================================
        // Đảm bảo các object này luôn tồn tại để tránh lỗi template
        if (!model.containsAttribute("selectMenuRequest")) {
            model.addAttribute("selectMenuRequest", new CreateSelectMenuRequest());
        }
        if (!model.containsAttribute("reservation")) {
            model.addAttribute("reservation", new CreateReservationRequest());
        }

        // ======================================
        // BƯỚC 3: TRẢ VỀ VIEW SALES.HTML
        // ======================================
        return "sales/sales";
    }



    // ================================================================================================
    // CHỨC NĂNG 2: XEM CHI TIẾT BÀN VÀ ORDER
    // ================================================================================================

    /**
     * 📋 XEM CHI TIẾT ORDER CỦA BÀN (Order Details Modal)
     *
     * Endpoint: GET /sale/view-detail?tableId={id}
     *
     * 🎯 Mục đích: Hiển thị thông tin chi tiết order/invoice của bàn trong popup
     *
     * 📋 Quy trình xử lý:
     * 1. Validate tableId và lấy thông tin bàn
     * 2. Tìm reservation hiện tại của bàn (nếu có)
     * 3. Nếu không có reservation → hiển thị lỗi "không có thông tin"
     * 4. Lấy invoice và invoice details từ reservation
     * 5. Map dữ liệu sang OrderDetailResponse DTO
     * 6. Hiển thị popup chi tiết trong sales.html
     *
     * 🔄 Trạng thái bàn hỗ trợ: RESERVED, OCCUPIED
     *
     * 📊 Thông tin hiển thị:
     * - Thông tin bàn và trạng thái
     * - Thông tin khách hàng (từ reservation)
     * - Danh sách món đã order với số lượng và giá
     * - Tổng tiền của hóa đơn
     */
    @GetMapping("/view-detail")
    public String getViewDetail(@RequestParam Integer tableId, Model model) {
        // BƯỚC 1: Validate và lấy thông tin bàn
        TableEntity table = tableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bàn với ID: " + tableId));

        // BƯỚC 2: Tìm reservation hiện tại của bàn
        ReservationEntity reservation = reservationService.findCurrentReservationByTableId(tableId);

        // BƯỚC 3: Kiểm tra tồn tại reservation
        if (reservation == null) {
            // Nếu không có reservation (bàn trống), hiển thị thông báo lỗi
            model.addAttribute("tables", tableRepository.findAll());
            model.addAttribute("showOrderDetailModal", false);
            model.addAttribute("orderDetailError", "Không có thông tin đặt bàn/order cho bàn này!");
            return "sales/sales";
        }

        // BƯỚC 4: Lấy thông tin invoice và invoice details
        InvoiceEntity invoice = reservation.getInvoice();
        List<InvoiceDetailEntity> invoiceDetails = invoiceDetailRepository.findAllByInvoice_IdAndIsDeletedFalse(invoice.getId());

        // BƯỚC 5: Map entity sang response DTO để hiển thị
        OrderDetailRessponse orderDetail = orderDetailMapper.toOrderDetailResponse(table, invoice, reservation,
                invoiceDetails);

        // BƯỚC 6: Truyền data cho view và hiển thị popup
        model.addAttribute("tables", tableRepository.findAll());
        model.addAttribute("orderDetail", orderDetail);
        model.addAttribute("showOrderDetailModal", true);
        return "sales/sales";
    }

    // ================================================================================================
    // CHỨC NĂNG 3: HIỂN THỊ FORM THANH TOÁN RIÊNG BIỆT (PAYMENT MODAL)
    // ================================================================================================

    @GetMapping("/show-payment-modal")
    public String showPaymentModal(@RequestParam Integer tableId, Model model) {
        // BƯỚC 1: Validate và lấy thông tin bàn
        TableEntity table = tableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bàn với ID: " + tableId));

        // BƯỚC 2: Kiểm tra trạng thái bàn - chỉ cho phép thanh toán bàn OCCUPIED
        if (table.getStatus() != TableStatus.OCCUPIED) {
            model.addAttribute("tables", tableRepository.findAll());
            model.addAttribute("errorMessage", "Chỉ có thể thanh toán bàn đang sử dụng (OCCUPIED)!");
            return "sales/sales";
        }

        // BƯỚC 3: Tìm reservation hiện tại của bàn
        var reservation = reservationService.findCurrentReservationByTableId(tableId);
        if (reservation == null) {
            model.addAttribute("tables", tableRepository.findAll());
            model.addAttribute("errorMessage", "Không có thông tin đặt bàn để thanh toán!");
            return "sales/sales";
        }

        // BƯỚC 4: Lấy invoice và chi tiết hóa đơn
        var invoice = reservation.getInvoice();
        if (invoice == null) {
            model.addAttribute("tables", tableRepository.findAll());
            model.addAttribute("errorMessage", "Không có hóa đơn để thanh toán!");
            return "sales/sales";
        }
        var invoiceDetails = invoiceDetailRepository.findAllByInvoice_IdAndIsDeletedFalse(invoice.getId());

        // BƯỚC 5: Map entity sang response DTO để hiển thị
        OrderDetailRessponse orderDetail = orderDetailMapper.toOrderDetailResponse(table, invoice, reservation,
                invoiceDetails);

        // BƯỚC 6: Truyền data cho view và hiển thị modal thanh toán
        model.addAttribute("tables", tableRepository.findAll());
        model.addAttribute("orderDetail", orderDetail);
        model.addAttribute("showPaymentModal", true);
        return "sales/sales";
    }

    /**
     * ====== XÁC NHẬN THANH TOÁN (PAY INVOICE) ======
     * Endpoint: POST /sale/pay-invoice
     * Nhận vào tableId, cập nhật trạng thái hóa đơn, reservation, bàn khi thanh
     * toán.
     * Không lưu số tiền khách đưa/thối lại (xử lý ở frontend).
     * Chỉ cập nhật trạng thái: Invoice -> PAID, Reservation -> xóa mềm, Table ->
     * AVAILABLE.
     */
    @PostMapping("/pay-invoice")
    public String payInvoice(@RequestParam Integer tableId, Model model, RedirectAttributes redirectAttributes) {
        try {
            // 1. Validate và lấy thông tin bàn
            var table = tableRepository.findById(tableId)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bàn với ID: " + tableId));

            // 2. Tìm reservation hiện tại của bàn
            var reservation = reservationService.findCurrentReservationByTableId(tableId);
            if (reservation == null) {
                model.addAttribute("tables", tableRepository.findAll());
                model.addAttribute("errorMessage", "Không có thông tin đặt bàn để thanh toán!");
                return "sales/sales";
            }

            // 3. Lấy invoice
            var invoice = reservation.getInvoice();
            if (invoice == null) {
                model.addAttribute("tables", tableRepository.findAll());
                model.addAttribute("errorMessage", "Không có hóa đơn để thanh toán!");
                return "sales/sales";
            }

            // 4. Cập nhật trạng thái: Invoice -> PAID (enum), Table -> AVAILABLE,
            // Reservation -> xóa mềm
            invoice.setStatus(com.viettridao.cafe.common.InvoiceStatus.PAID); // dùng enum đúng
            reservation.setIsDeleted(true); // xóa mềm reservation
            table.setStatus(TableStatus.AVAILABLE);

            // 5. Lưu lại các entity
            reservationService.saveReservationAndRelated(reservation, invoice, table);

            // 6. Thành công: redirect về trang chính với thông báo
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

    // ================================================================================================
    // CHỨC NĂNG 4: CHỌN THỰC ĐƠN (SELECT MENU)
    // ================================================================================================

    @GetMapping("/show-select-menu-form")
    public String showSelectMenuForm(@RequestParam(value = "tableId", required = false) Integer tableId, Model model) {
        // Nếu không có tableId, trả về sales với thông báo lỗi
        if (tableId == null) {
            model.addAttribute("errorMessage", "Bạn chưa chọn bàn để thực hiện chức năng này!");
            model.addAttribute("tables", tableRepository.findAll());
            return "sales/sales";
        }

        // BƯỚC 1: Validate và lấy thông tin bàn
        var table = tableRepository.findById(tableId)
                .orElse(null);
        if (table == null) {
            model.addAttribute("errorMessage", "Không tìm thấy bàn với ID: " + tableId);
            model.addAttribute("tables", tableRepository.findAll());
            return "sales/sales";
        }

        // BƯỚC 2: Khởi tạo request object cho form binding
        CreateSelectMenuRequest selectMenuRequest = new CreateSelectMenuRequest();
        selectMenuRequest.setTableId(tableId);
        selectMenuRequest.setItems(new ArrayList<>()); // Khởi tạo list để tránh null pointer

        // BƯỚC 3: Load thông tin khách hàng nếu bàn đã có reservation
        if (table.getStatus().name().equals("RESERVED") || table.getStatus().name().equals("OCCUPIED")) {
            try {
                var reservation = reservationService.findCurrentReservationByTableId(tableId);
                if (reservation != null) {
                    // Pre-fill thông tin khách hàng từ reservation hiện tại
                    selectMenuRequest.setCustomerName(reservation.getCustomerName());
                    selectMenuRequest.setCustomerPhone(reservation.getCustomerPhone());
                }
            } catch (Exception e) {
                // Không có reservation hoặc lỗi → để trống để người dùng nhập thủ công
                // Log error nhưng không throw exception để không làm gián đoạn flow
            }
        }

        // BƯỚC 4: Lấy danh sách menu items từ service
        var menuItems = selectMenuService.getMenuItems();

        // BƯỚC 5: Chuẩn bị data cho view - hiển thị popup trong sales.html
        model.addAttribute("tables", tableRepository.findAll()); // Danh sách tất cả bàn
        model.addAttribute("selectMenuRequest", selectMenuRequest); // Form request object
        model.addAttribute("selectedTable", table); // Bàn đang được chọn
        model.addAttribute("showSelectMenuForm", true); // Flag hiển thị popup
        model.addAttribute("menuItems", menuItems); // Danh sách menu items

        // BƯỚC 6: Trả về trang sales với popup chọn thực đơn
        return "sales/sales";
    }


    @PostMapping("/select-menu-on-sales")
    public String selectMenuOnSales(
            @Valid @ModelAttribute("selectMenuRequest") CreateSelectMenuRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        TableEntity table = null;
        try {
            // BƯỚC 1: Kiểm tra tableId hợp lệ
            if (request.getTableId() == null) {
                bindingResult.reject("error.tableId", "Không xác định được bàn để chọn món.");
            } else {
                table = tableRepository.findById(request.getTableId()).orElse(null);
                if (table == null) {
                    bindingResult.reject("error.tableId", "Không tìm thấy bàn với ID: " + request.getTableId());
                }
            }

            // BƯỚC 2: Validate thông tin khách hàng theo trạng thái bàn
            if (table != null && table.getStatus().name().equals("AVAILABLE")) {
                if (request.getCustomerName() == null || request.getCustomerName().trim().isEmpty()) {
                    bindingResult.rejectValue("customerName", "error.customerName",
                            "Tên khách hàng không được để trống khi tạo mới order");
                }
                if (request.getCustomerPhone() == null || request.getCustomerPhone().trim().isEmpty()) {
                    bindingResult.rejectValue("customerPhone", "error.customerPhone",
                            "Số điện thoại không được để trống khi tạo mới order");
                }
            }

            // BƯỚC 3: Validate món đã chọn
            if (request.getItems() == null || request.getItems().isEmpty()) {
                bindingResult.reject("error.items", "Vui lòng chọn ít nhất một món");
            } else {
                var validItems = request.getItems().stream()
                        .filter(item -> item.getMenuItemId() != null && item.getQuantity() != null
                                && item.getQuantity() > 0)
                        .toList();

                if (validItems.isEmpty()) {
                    bindingResult.reject("error.items", "Vui lòng chọn ít nhất một món và nhập số lượng");
                } else {
                    request.setItems(new ArrayList<>(validItems));
                }
            }

            // Nếu có lỗi validation, trả lại form với lỗi
            if (bindingResult.hasErrors()) {
                model.addAttribute("tables", tableRepository.findAll());
                model.addAttribute("selectMenuRequest", request);
                model.addAttribute("selectedTable", table);
                model.addAttribute("showSelectMenuForm", true);
                model.addAttribute("org.springframework.validation.BindingResult.selectMenuRequest", bindingResult);
                model.addAttribute("menuItems", selectMenuService.getMenuItems());
                return "sales/sales";
            }

            // LẤY THÔNG TIN NHÂN VIÊN TỪ SECURITY CONTEXT
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            AccountEntity account = accountRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Không tìm thấy thông tin nhân viên cho tài khoản đăng nhập: " + username));
            if (account.getEmployee() == null) {
                throw new IllegalArgumentException("Tài khoản '" + username + "' không liên kết với nhân viên nào!");
            }
            Integer employeeId = account.getEmployee().getId();

            // GỌI SERVICE XỬ LÝ NGHIỆP VỤ
            OrderDetailRessponse orderDetail = selectMenuService.createOrderForAvailableTable(request, employeeId);

            // SUCCESS
            model.addAttribute("tables", tableRepository.findAll());
            model.addAttribute("successMessage", "Chọn món thành công!");
            model.addAttribute("orderDetail", orderDetail);
            model.addAttribute("showSelectMenuForm", false); // Ẩn form sau khi thành công
            return "sales/sales";
        } catch (IllegalArgumentException e) {
            bindingResult.reject("error.system", e.getMessage());
        } catch (RuntimeException e) {
            bindingResult.reject("error.system", "Đã xảy ra lỗi hệ thống: " + e.getMessage());
        }

        // Trả lại form nếu có lỗi trong catch
        model.addAttribute("tables", tableRepository.findAll());
        model.addAttribute("selectMenuRequest", request);
        model.addAttribute("selectedTable", table);
        model.addAttribute("showSelectMenuForm", true);
        model.addAttribute("org.springframework.validation.BindingResult.selectMenuRequest", bindingResult);
        model.addAttribute("menuItems", selectMenuService.getMenuItems());
        return "sales/sales";
    }

    // ================================================================================================
    // CHỨC NĂNG 5: ĐẶT BÀN (RESERVATION)
    // ================================================================================================

    @GetMapping("/show-reservation-form")
    public String showReservationForm(@RequestParam Integer tableId, Model model) {
        // BƯỚC 1: Tạo reservation request với tableId được chọn
        CreateReservationRequest reservation = new CreateReservationRequest();
        reservation.setTableId(tableId);

        // BƯỚC 2: Chuẩn bị data cho view - hiển thị popup trong sales.html
        model.addAttribute("tables", tableRepository.findAll()); // Danh sách tất cả bàn
        model.addAttribute("reservation", reservation); // Form request object
        model.addAttribute("showReservationForm", true); // Flag hiển thị popup

        // BƯỚC 3: Trả về trang sales với popup đặt bàn
        return "sales/sales";
    }

    @PostMapping("/reservations")
    public String createReservation(@Valid @ModelAttribute("reservation") CreateReservationRequest request,
                                    BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        try {
            // ========================================
            // BƯỚC 1: SPRING FRAMEWORK VALIDATION
            // ========================================
            if (bindingResult.hasErrors()) {
                // Khi có lỗi validate, trả về sales.html với form đặt bàn hiển thị
                model.addAttribute("tables", tableRepository.findAll());
                model.addAttribute("reservation", request);
                model.addAttribute("showReservationForm", true);
                return "sales/sales";
            }

            // ========================================
            // BƯỚC 2: LẤY THÔNG TIN NHÂN VIÊN TỪ SECURITY CONTEXT
            // ========================================
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            AccountEntity account = accountRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Không tìm thấy thông tin nhân viên cho tài khoản đăng nhập: " + username));
            if (account.getEmployee() == null) {
                throw new IllegalArgumentException("Tài khoản '" + username + "' không liên kết với nhân viên nào!");
            }
            Integer employeeId = account.getEmployee().getId();

            // ========================================
            // BƯỚC 3: GỌI SERVICE THỰC HIỆN BUSINESS LOGIC
            // ========================================
            // Service sẽ xử lý: validate bàn AVAILABLE, tạo reservation, tạo invoice rỗng,
            // cập nhật trạng thái bàn
            reservationService.createReservation(request, employeeId);

            // ========================================
            // BƯỚC 4: SUCCESS - REDIRECT VỚI THÔNG BÁO THÀNH CÔNG
            // ========================================
            redirectAttributes.addFlashAttribute("success", "Đặt bàn thành công!");
            return "redirect:/sale";

        } catch (IllegalArgumentException e) {
            // Lỗi nghiệp vụ từ service layer
            bindingResult.rejectValue("customerName", "error.customerName", e.getMessage());
        } catch (RuntimeException e) {
            // Lỗi hệ thống không mong muốn
            bindingResult.reject("error.system", "Đã xảy ra lỗi hệ thống: " + e.getMessage());
        }

        // ========================================
        // BƯỚC 5: ERROR - TRẢ VỀ FORM VỚI LỖI
        // ========================================
        // Khi có exception, trả về sales.html với form đặt bàn hiển thị và lỗi
        model.addAttribute("tables", tableRepository.findAll());
        model.addAttribute("reservation", request);
        model.addAttribute("showReservationForm", true);
        return "sales/sales";
    }

    // ================================================================================================
    // CHỨC NĂNG 6: HỦY BÀN (CANCEL RESERVATION)
    // ================================================================================================

    @GetMapping("/show-cancel-reservation-form")
    public String showCancelReservationForm(@RequestParam Integer tableId, Model model) {
        // BƯỚC 1: Validate và lấy thông tin bàn
        var table = tableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bàn với ID: " + tableId));

        // BƯỚC 2: Kiểm tra bàn có reservation không
        var reservation = reservationService.findCurrentReservationByTableId(tableId);
        if (reservation == null || Boolean.TRUE.equals(reservation.getIsDeleted())) {
            model.addAttribute("tables", tableRepository.findAll());
            model.addAttribute("errorMessage", "Không tìm thấy thông tin đặt bàn để hủy!");
            return "sales/sales";
        }

        // BƯỚC 3: Validate business rules - chỉ cho phép hủy khi bàn đang RESERVED
        if (!"RESERVED".equals(reservation.getTable().getStatus().name())) {
            model.addAttribute("tables", tableRepository.findAll());
            model.addAttribute("errorMessage", "Chỉ có thể hủy bàn ở trạng thái ĐÃ ĐẶT (RESERVED)!");
            return "sales/sales";
        }

        // BƯỚC 4: Truyền thông tin để hiển thị form xác nhận hủy bàn
        model.addAttribute("tables", tableRepository.findAll());
        model.addAttribute("selectedTable", table);
        model.addAttribute("reservation", reservation);
        model.addAttribute("showCancelReservationForm", true);
        return "sales/sales";
    }


    @PostMapping("/cancel-reservation")
    public String cancelReservation(@RequestParam Integer tableId, Model model) {
        // Lấy reservation hiện tại (chỉ lấy reservation chưa bị xóa mềm, trạng thái
        // RESERVED)
        var reservation = reservationService.findCurrentReservationByTableId(tableId);
        if (reservation == null || Boolean.TRUE.equals(reservation.getIsDeleted())) {
            model.addAttribute("tables", tableRepository.findAll());
            model.addAttribute("errorMessage", "Không tìm thấy thông tin đặt bàn để hủy!");
            model.addAttribute("hideCancelModal", true);
            return "sales/sales";
        }
        // Chỉ cho phép hủy khi bàn đang RESERVED
        if (!"RESERVED".equals(reservation.getTable().getStatus().name())) {
            model.addAttribute("tables", tableRepository.findAll());
            model.addAttribute("errorMessage", "Chỉ có thể hủy bàn ở trạng thái ĐÃ ĐẶT!");
            model.addAttribute("hideCancelModal", true);
            return "sales/sales";
        }
        // Đặt isDeleted = true cho reservation
        reservation.setIsDeleted(true);
        // Đặt isDeleted = true cho invoice
        var invoice = reservation.getInvoice();
        if (invoice != null) {
            invoice.setIsDeleted(true);
            // Đặt isDeleted = true cho tất cả invoice detail liên quan
            if (invoice.getId() != null) {
                var invoiceDetails = invoiceDetailRepository.findAllByInvoice_IdAndIsDeletedFalse(invoice.getId());
                for (var detail : invoiceDetails) {
                    detail.setIsDeleted(true);
                }
                invoiceDetailRepository.saveAll(invoiceDetails);
            }
        }
        // Đặt trạng thái bàn về AVAILABLE
        var table = reservation.getTable();
        table.setStatus(TableStatus.AVAILABLE);
        // Lưu lại các entity
        reservationService.saveReservationAndRelated(reservation, invoice, table);
        model.addAttribute("tables", tableRepository.findAll());
        model.addAttribute("successMessage", "Hủy bàn thành công!");
        model.addAttribute("hideCancelModal", true); // JS sẽ dùng biến này để ẩn modal
        return "sales/sales";
    }

    // ================================================================================================
    // CHỨC NĂNG 7: CHUYỂN BÀN (MOVE TABLE)
    // ================================================================================================

    @GetMapping("/show-move-table-form")
    public String showMoveTableForm(@RequestParam Integer selectedTableId, Model model) {
        try {
            // VALIDATE THÔNG TIN BÀN NGUỒN
            var sourceTableOpt = tableRepository.findById(selectedTableId);
            if (sourceTableOpt.isEmpty()) {
                model.addAttribute("errorMessage", "Không tìm thấy bàn nguồn với ID: " + selectedTableId);
                return "sales/sales";
            }
            var sourceTable = sourceTableOpt.get();
            // Chỉ chuyển được từ bàn OCCUPIED
            if (sourceTable.getStatus() != TableStatus.OCCUPIED) {
                model.addAttribute("errorMessage",
                        "Chỉ có thể chuyển từ bàn đang sử dụng (OCCUPIED). Bàn hiện tại: " + sourceTable.getStatus());
                return "sales/sales";
            }
            // KIỂM TRA RESERVATION
            var sourceReservation = reservationService.findCurrentReservationByTableId(selectedTableId);
            if (sourceReservation == null) {
                model.addAttribute("errorMessage", "Không tìm thấy thông tin đặt bàn cho bàn nguồn");
                return "sales/sales";
            }
            // LẤY DANH SÁCH BÀN ĐÍCH KHẢ DỤNG
            var allTables = tableRepository.findAll();
            var availableTables = allTables.stream()
                    .filter(table -> table.getStatus() == TableStatus.AVAILABLE)
                    .toList();
            if (availableTables.isEmpty()) {
                model.addAttribute("errorMessage", "Không có bàn trống nào để chuyển đến");
                return "sales/sales";
            }
            // TRUYỀN DỮ LIỆU CHO VIEW
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


    @PostMapping("/move-table")
    public String moveTable(
            @RequestParam(required = false) Integer sourceTableId,
            @RequestParam(required = false) Integer targetTableId,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            // Validate input cơ bản
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

            // Tạo request object từ parameters
            com.viettridao.cafe.dto.request.sales.MoveTableRequest request = new com.viettridao.cafe.dto.request.sales.MoveTableRequest();
            request.setSourceTableId(sourceTableId);
            request.setTargetTableId(targetTableId);

            // Lấy employeeId từ session
            Integer employeeId = getCurrentEmployeeId();

            // Gọi service chuyển bàn
            reservationService.moveTable(request, employeeId);

            // Thành công
            redirectAttributes.addFlashAttribute("successMessage", "Chuyển bàn thành công!");
            return "redirect:/sale";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "Đã xảy ra lỗi hệ thống: " + e.getMessage());
        }
        // Trả lại form nếu có lỗi
        model.addAttribute("tables", tableRepository.findAll());
        model.addAttribute("showMoveModal", true);
        model.addAttribute("selectedTableId", sourceTableId);
        // Thêm object mặc định để tránh lỗi template
        model.addAttribute("reservation", new CreateReservationRequest());
        model.addAttribute("selectMenuRequest", new CreateSelectMenuRequest());
        return "sales/sales";
    }

    // ================================================================================================
    // CHỨC NĂNG 8: GỘP BÀN (MERGE TABLE)
    // ================================================================================================
    @GetMapping("/show-merge-table-form")
    public String showMergeTableForm(@RequestParam Integer selectedTableId, Model model) {
        // Lấy danh sách các bàn OCCUPIED để hiển thị trong modal gộp bàn
        List<TableEntity> occupiedTables = tableRepository.findAll().stream()
                .filter(table -> table.getStatus() == TableStatus.OCCUPIED)
                .toList();

        model.addAttribute("showMergeModal", true);
        model.addAttribute("occupiedTables", occupiedTables);
        model.addAttribute("selectedTableId", selectedTableId);

        return "sales/sales";
    }


    /**
     * Xử lý gộp bàn (merge tables) - POST
     */
    @PostMapping("/merge-tables")
    public String mergeTables(@Valid @ModelAttribute("mergeTableRequest") MergeTableRequest request,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        try {
            // Validate DTO
            if (bindingResult.hasErrors()) {
                var occupiedTables = tableRepository.findAll().stream()
                        .filter(table -> table.getStatus() == TableStatus.OCCUPIED)
                        .toList();
                model.addAttribute("tables", tableRepository.findAll());
                model.addAttribute("mergeTableRequest", request);
                model.addAttribute("showMergeModal", true);
                model.addAttribute("occupiedTables", occupiedTables);
                model.addAttribute("org.springframework.validation.BindingResult.mergeTableRequest", bindingResult);
                return "sales/sales";
            }
            // Lấy employeeId từ session
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            AccountEntity account = accountRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Không tìm thấy thông tin nhân viên cho tài khoản đăng nhập!"));
            if (account.getEmployee() == null) {
                throw new IllegalArgumentException("Không tìm thấy thông tin nhân viên cho tài khoản đăng nhập!");
            }
            Integer employeeId = account.getEmployee().getId();
            // Gọi service gộp bàn
            reservationService.mergeTables(request, employeeId);
            // Thành công
            redirectAttributes.addFlashAttribute("successMessage", "Gộp bàn thành công!");
            return "redirect:/sale";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "Đã xảy ra lỗi hệ thống: " + e.getMessage());
        }
        // Trả lại form nếu có lỗi
        var occupiedTables = tableRepository.findAll().stream()
                .filter(table -> table.getStatus() == TableStatus.OCCUPIED)
                .toList();
        model.addAttribute("tables", tableRepository.findAll());
        model.addAttribute("mergeTableRequest", request);
        model.addAttribute("showMergeModal", true);
        model.addAttribute("occupiedTables", occupiedTables);
        model.addAttribute("org.springframework.validation.BindingResult.mergeTableRequest", bindingResult);
        return "sales/sales";
    }



    // ================================================================================================
    // CHỨC NĂNG 9: TÁCH BÀN (SPLIT TABLE)
    // ================================================================================================

    @GetMapping("/show-split-table-form")
    public String showSplitTableForm(@RequestParam Integer selectedTableId, Model model) {
        try {
            // 1. VALIDATE BÀN NGUỒN
            var sourceTableOpt = tableRepository.findById(selectedTableId);
            if (sourceTableOpt.isEmpty()) {
                model.addAttribute("errorMessage", "Không tìm thấy bàn nguồn với ID: " + selectedTableId);
                return "sales/sales";
            }
            var sourceTable = sourceTableOpt.get();

            // 2. CHỈ CHO PHÉP TÁCH TỪ BÀN OCCUPIED
            if (sourceTable.getStatus() != TableStatus.OCCUPIED) {
                model.addAttribute("errorMessage",
                        "Chỉ có thể tách từ bàn đang sử dụng (OCCUPIED). Bàn hiện tại: " + sourceTable.getStatus());
                return "sales/sales";
            }

            // 3. KIỂM TRA RESERVATION & INVOICE
            var sourceReservation = reservationService.findCurrentReservationByTableId(selectedTableId);
            if (sourceReservation == null) {
                model.addAttribute("errorMessage", "Không tìm thấy thông tin đặt bàn cho bàn nguồn");
                return "sales/sales";
            }
            if (sourceReservation.getInvoice() == null) {
                model.addAttribute("errorMessage", "Không tìm thấy hóa đơn cho bàn nguồn");
                return "sales/sales";
            }

            // 4. LẤY DANH SÁCH MÓN ĐỂ TÁCH
            var invoiceDetails = invoiceDetailRepository
                    .findAllByInvoice_IdAndIsDeletedFalse(sourceReservation.getInvoice().getId());
            if (invoiceDetails.isEmpty()) {
                model.addAttribute("errorMessage", "Bàn nguồn không có món nào để tách");
                return "sales/sales";
            }

            // 5. LẤY DANH SÁCH BÀN ĐÍCH KHẢ DỤNG
            var allTables = tableRepository.findAll();
            var availableTables = allTables.stream()
                    .filter(table -> table.getStatus() == TableStatus.AVAILABLE)
                    .toList();
            var occupiedTables = allTables.stream()
                    .filter(table -> table.getStatus() == TableStatus.OCCUPIED
                            && !table.getId().equals(selectedTableId))
                    .toList();

            if (availableTables.isEmpty() && occupiedTables.isEmpty()) {
                model.addAttribute("errorMessage", "Không có bàn nào khả dụng để tách đến");
                return "sales/sales";
            }

            // 6. TẠO OBJECT CHO FORM
            SplitTableRequest splitRequest = new SplitTableRequest();
            splitRequest.setSourceTableId(selectedTableId);

            List<SplitTableRequest.SplitItemRequest> items = new ArrayList<>();
            for (var detail : invoiceDetails) {
                SplitTableRequest.SplitItemRequest item = new SplitTableRequest.SplitItemRequest();
                item.setMenuItemId(detail.getMenuItem().getId());
                item.setQuantity(0); // Mặc định số lượng = 0
                items.add(item);
            }
            splitRequest.setItems(items);

            // 7. TRUYỀN DATA CHO VIEW
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

    @PostMapping("/split-table")
    public String splitTable(@Valid @ModelAttribute SplitTableRequest request,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        try {
            // ====== 1. VALIDATE CƠ BẢN VÀ SPRING VALIDATION ======
            if (bindingResult.hasErrors()) {

                // Chuẩn bị lại dữ liệu cho view khi có lỗi validation
                return setupSplitModalOnError(request, model, bindingResult, "Lỗi validation dữ liệu đầu vào");
            }

            // ====== 2. VALIDATE NGHIỆP VỤ BỔ SUNG ======
            // Kiểm tra có chọn món nào không
            if (request.getItems() == null || request.getItems().isEmpty()) {
                return setupSplitModalOnError(request, model, bindingResult, "Vui lòng chọn ít nhất một món để tách");
            }

            // Lọc các món có số lượng hợp lệ (> 0)
            var validItems = request.getItems().stream()
                    .filter(item -> item.getMenuItemId() != null && item.getQuantity() != null
                            && item.getQuantity() > 0)
                    .toList();

            if (validItems.isEmpty()) {
                return setupSplitModalOnError(request, model, bindingResult,
                        "Vui lòng nhập số lượng hợp lệ cho các món được chọn");
            }

            // Cập nhật lại items chỉ gồm các món hợp lệ
            request.setItems(new ArrayList<>(validItems));

            // ====== 3. LẤY THÔNG TIN NHÂN VIÊN THỰC HIỆN ======
            Integer employeeId = getCurrentEmployeeId();

            // ====== 4. GỌI SERVICE THỰC HIỆN TÁCH BÀN ======
            System.out.println("Calling reservation service to split table...");
            reservationService.splitTable(request, employeeId);

            // ====== 5. THÀNH CÔNG - REDIRECT VỀ TRANG CHÍNH ======
            System.out.println("Split table completed successfully");
            redirectAttributes.addFlashAttribute("successMessage",
                    "Tách bàn thành công! Đã chuyển " + validItems.size() + " món từ bàn nguồn sang bàn đích.");
            return "redirect:/sale";

        } catch (IllegalArgumentException e) {
            // Lỗi nghiệp vụ từ service layer
            return setupSplitModalOnError(request, model, bindingResult, e.getMessage());

        } catch (RuntimeException e) {
            // Lỗi hệ thống không mong muốn
            return setupSplitModalOnError(request, model, bindingResult,
                    "Đã xảy ra lỗi hệ thống: " + e.getMessage());
        }
    }















    /**
     * ====== HELPER METHOD: LẤY ID NHÂN VIÊN HIỆN TẠI ======
     *
     * Lấy thông tin nhân viên từ session đăng nhập hiện tại
     */
    private Integer getCurrentEmployeeId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        AccountEntity account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy thông tin tài khoản cho username: " + username));

        if (account.getEmployee() == null) {
            throw new IllegalArgumentException(
                    "Tài khoản '" + username + "' không liên kết với nhân viên nào!");
        }

        return account.getEmployee().getId();
    }


    /**
     * ====== HELPER METHOD: THIẾT LẬP LẠI MODAL TÁCH BÀN KHI CÓ LỖI ======
     *
     * Method này được gọi khi có lỗi trong quá trình xử lý tách bàn
     * để chuẩn bị lại toàn bộ dữ liệu cần thiết cho view hiển thị form
     */
    private String setupSplitModalOnError(SplitTableRequest request, Model model,
                                          BindingResult bindingResult, String errorMessage) {

        try {
            // Lấy tất cả bàn
            var allTables = tableRepository.findAll();

            // Lọc bàn trống (AVAILABLE)
            var availableTables = allTables.stream()
                    .filter(table -> table.getStatus() == TableStatus.AVAILABLE)
                    .toList();

            // Lọc bàn đang sử dụng (OCCUPIED) trừ bàn nguồn
            var occupiedTables = allTables.stream()
                    .filter(table -> table.getStatus() == TableStatus.OCCUPIED
                            && !table.getId().equals(request.getSourceTableId()))
                    .toList();

            // Lấy thông tin bàn nguồn
            var sourceTable = tableRepository.findById(request.getSourceTableId()).orElse(null);

            // Lấy thông tin reservation và invoice details của bàn nguồn
            var sourceReservation = reservationService.findCurrentReservationByTableId(request.getSourceTableId());
            List<com.viettridao.cafe.model.InvoiceDetailEntity> invoiceDetails = new ArrayList<>();
            if (sourceReservation != null && sourceReservation.getInvoice() != null) {
                invoiceDetails = invoiceDetailRepository
                        .findAllByInvoice_IdAndIsDeletedFalse(sourceReservation.getInvoice().getId());
            }

            // Truyền tất cả dữ liệu cần thiết cho view
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