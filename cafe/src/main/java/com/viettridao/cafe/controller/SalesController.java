package com.viettridao.cafe.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.viettridao.cafe.model.AccountEntity;
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
    // CHỨC NĂNG 1: CHỌN THỰC ĐƠN (SELECT MENU)
    // ================================================================================================

    /**
     * 📋 HIỂN THỊ TRANG CHỌN THỰC ĐƒN RIÊNG BIỆT (Không sử dụng trong flow chính)
     * 
     * Endpoint: GET /sale/select-menu?tableId={id}
     * 
     * 🎯 Mục đích: Hiển thị trang chọn thực đơn riêng biệt (không phải popup)
     * 
     * 📋 Quy trình:
     * 1. Validate tableId có tồn tại không
     * 2. Tạo SelectMenuRequest với tableId
     * 3. Truyền data cho view select_menu.html
     * 
     * ⚠️ Lưu ý: Method này ít được sử dụng, chủ yếu dùng showSelectMenuForm()
     */
    @GetMapping("/select-menu")
    public String getSelectMenuPage(@RequestParam Integer tableId, Model model) {
        // BƯỚC 1: Validate và lấy thông tin bàn
        var table = tableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bàn với ID: " + tableId));

        // BƯỚC 2: Tạo request object mặc định cho form binding
        CreateSelectMenuRequest selectMenuRequest = new CreateSelectMenuRequest();
        selectMenuRequest.setTableId(tableId);

        // BƯỚC 3: Truyền data cho view template
        model.addAttribute("table", table);
        model.addAttribute("selectMenuRequest", selectMenuRequest);

        // BƯỚC 4: Trả về trang chọn thực đơn riêng biệt
        return "sales/select_menu";
    }

    /**
     * 🎯 HIỂN THỊ FORM CHỌN THỰC ĐƒN TRONG POPUP (Flow chính)
     * 
     * Endpoint: GET /sale/show-select-menu-form?tableId={id}
     * 
     * 🎯 Mục đích: Hiển thị form chọn thực đơn dạng popup trong trang sales.html
     * 
     * 📋 Quy trình:
     * 1. Validate tableId và lấy thông tin bàn
     * 2. Tạo SelectMenuRequest với tableId và khởi tạo items list
     * 3. Nếu bàn đã có reservation (RESERVED/OCCUPIED) → load thông tin khách hàng
     * 4. Lấy danh sách menu items từ service
     * 5. Set flag showSelectMenuForm = true để hiển thị popup
     * 6. Trả về sales.html với popup hiển thị
     * 
     * 🔄 Trạng thái bàn hỗ trợ: AVAILABLE, RESERVED, OCCUPIED
     * 
     * ⚡ Performance: Sử dụng try-catch để handle trường hợp không tìm thấy
     * reservation
     */
    @GetMapping("/show-select-menu-form")
    public String showSelectMenuForm(@RequestParam Integer tableId, Model model) {
        // BƯỚC 1: Validate và lấy thông tin bàn
        var table = tableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bàn với ID: " + tableId));

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

    /**
     * 💾 XỬ LÝ SUBMIT FORM CHỌN THỰC ĐƒN (Core Business Logic)
     * 
     * Endpoint: POST /sale/select-menu-on-sales
     * 
     * 🎯 Mục đích: Xử lý submit form chọn thực đơn với validation và business logic
     * 
     * 📋 Quy trình xử lý:
     * 1. VALIDATION LAYER 1: Spring Framework Validation (@Valid annotation)
     * 2. VALIDATION LAYER 2: Business Rules Validation
     * - Nếu bàn AVAILABLE → bắt buộc nhập thông tin khách hàng
     * - Bắt buộc chọn ít nhất 1 món có số lượng > 0
     * 3. SECURITY: Lấy thông tin nhân viên từ Security Context
     * 4. BUSINESS LOGIC: Gọi service tạo order
     * 5. SUCCESS: Hiển thị thông báo thành công + order details
     * 6. ERROR: Trả về form với thông báo lỗi cụ thể
     * 
     * 🔄 Trạng thái bàn xử lý:
     * - AVAILABLE → Tạo mới reservation + invoice + order
     * - RESERVED → Cập nhật reservation thành OCCUPIED + tạo order
     * - OCCUPIED → Thêm món vào order hiện tại
     * 
     * 🛡️ Error Handling:
     * - IllegalArgumentException: Lỗi nghiệp vụ (hiển thị cho user)
     * - RuntimeException: Lỗi hệ thống (log + thông báo generic)
     * 
     * ⚡ Performance Notes:
     * - Sử dụng @Transactional trong service để đảm bảo data consistency
     * - Stream API để filter items có quantity > 0
     * - Validation 2 lớp: Framework + Business Logic
     */
    @PostMapping("/select-menu-on-sales")
    public String selectMenuOnSales(@Valid @ModelAttribute("selectMenuRequest") CreateSelectMenuRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            // ========================================
            // BƯỚC 1: SPRING FRAMEWORK VALIDATION
            // ========================================
            // @Valid annotation đã validate các field theo annotation trong DTO
            // BindingResult chứa kết quả validation

            // ========================================
            // BƯỚC 2: BUSINESS RULES VALIDATION
            // ========================================

            // Sub-step 2.1: Lấy thông tin bàn để validate business rules
            var table = tableRepository.findById(request.getTableId())
                    .orElseThrow(
                            () -> new IllegalArgumentException("Không tìm thấy bàn với ID: " + request.getTableId()));

            // Sub-step 2.2: Validate thông tin khách hàng theo trạng thái bàn
            if (table.getStatus().name().equals("AVAILABLE")) {
                // Rule: Bàn trống bắt buộc nhập thông tin khách hàng
                if (request.getCustomerName() == null || request.getCustomerName().trim().isEmpty()) {
                    bindingResult.rejectValue("customerName", "error.customerName",
                            "Tên khách hàng không được để trống khi tạo mới order");
                }
                if (request.getCustomerPhone() == null || request.getCustomerPhone().trim().isEmpty()) {
                    bindingResult.rejectValue("customerPhone", "error.customerPhone",
                            "Số điện thoại không được để trống khi tạo mới order");
                }
            }

            // Sub-step 2.3: Validate món đã chọn
            if (request.getItems() == null || request.getItems().isEmpty()) {
                bindingResult.reject("error.items", "Vui lòng chọn ít nhất một món");
            } else {
                // Lọc các món có quantity > 0 (món thực sự được chọn)
                var validItems = request.getItems().stream()
                        .filter(item -> item.getMenuItemId() != null && item.getQuantity() != null
                                && item.getQuantity() > 0)
                        .toList();

                if (validItems.isEmpty()) {
                    bindingResult.reject("error.items", "Vui lòng chọn ít nhất một món và nhập số lượng");
                } else {
                    // Cập nhật lại items chỉ gồm các món hợp lệ (performance optimization)
                    request.setItems(new ArrayList<>(validItems));
                }
            }

            // ========================================
            // BƯỚC 3: XỬ LÝ VALIDATION ERRORS
            // ========================================
            if (bindingResult.hasErrors()) {
                // Chuẩn bị lại data cho view khi có lỗi validation
                var tableForError = tableRepository.findById(request.getTableId()).orElse(null);

                model.addAttribute("tables", tableRepository.findAll());
                model.addAttribute("selectMenuRequest", request);
                model.addAttribute("selectedTable", tableForError);
                model.addAttribute("showSelectMenuForm", true);
                model.addAttribute("org.springframework.validation.BindingResult.selectMenuRequest", bindingResult);
                model.addAttribute("menuItems", selectMenuService.getMenuItems());

                // Trả về trang sales với form hiển thị và errors
                return "sales/sales";
            }

            // ========================================
            // BƯỚC 4: LẤY THÔNG TIN NHÂN VIÊN TỪ SECURITY CONTEXT
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
            // BƯỚC 5: GỌI SERVICE THỰC HIỆN BUSINESS LOGIC
            // ========================================
            // Service sẽ xử lý logic theo trạng thái bàn (AVAILABLE/RESERVED/OCCUPIED)
            OrderDetailRessponse orderDetail = selectMenuService.createOrderForAvailableTable(request, employeeId);

            // ========================================
            // BƯỚC 6: SUCCESS - HIỂN THỊ KẾT QUẢ THÀNH CÔNG
            // ========================================
            model.addAttribute("tables", tableRepository.findAll());
            model.addAttribute("successMessage", "Chọn món thành công!");
            model.addAttribute("orderDetail", orderDetail);
            model.addAttribute("showSelectMenuForm", false); // Ẩn form sau khi thành công
            return "sales/sales";
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("customerName", "error.customerName", e.getMessage());
        } catch (RuntimeException e) {
            bindingResult.reject("error.system", "Đã xảy ra lỗi hệ thống: " + e.getMessage());
        }
        // Trả lại form nếu có lỗi
        var tableForFinalError = tableRepository.findById(request.getTableId()).orElse(null);
        model.addAttribute("tables", tableRepository.findAll());
        model.addAttribute("selectMenuRequest", request);
        model.addAttribute("selectedTable", tableForFinalError);
        model.addAttribute("showSelectMenuForm", true);
        model.addAttribute("org.springframework.validation.BindingResult.selectMenuRequest", bindingResult);
        model.addAttribute("menuItems", selectMenuService.getMenuItems());
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
        var table = tableRepository.findById(tableId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bàn với ID: " + tableId));

        // BƯỚC 2: Tìm reservation hiện tại của bàn
        var reservation = reservationService.findCurrentReservationByTableId(tableId);

        // BƯỚC 3: Kiểm tra tồn tại reservation
        if (reservation == null) {
            // Nếu không có reservation (bàn trống), hiển thị thông báo lỗi
            model.addAttribute("tables", tableRepository.findAll());
            model.addAttribute("showOrderDetailModal", false);
            model.addAttribute("orderDetailError", "Không có thông tin đặt bàn/order cho bàn này!");
            return "sales/sales";
        }

        // BƯỚC 4: Lấy thông tin invoice và invoice details
        var invoice = reservation.getInvoice();
        var invoiceDetails = invoiceDetailRepository.findAllByInvoice_IdAndIsDeletedFalse(invoice.getId());

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
    // CHỨC NĂNG 3: HIỂN THỊ TRANG CHÍNH SALES OVERVIEW
    // ================================================================================================

    /**
     * 🏠 TRANG CHÍNH QUẢN LÝ BÁN HÀNG VÀ CÁC MODAL
     * 
     * Endpoint: GET /sale
     * 
     * 🎯 Mục đích:
     * - Hiển thị danh sách tất cả bàn với trạng thái
     * - Xử lý hiển thị các modal (gộp bàn, tách bàn) dựa trên query parameters
     * 
     * 📋 Query Parameters:
     * - showMergeModal: Hiển thị modal gộp bàn
     * - showSplitModal: Hiển thị modal tách bàn
     * - selectedTableId: ID bàn được chọn để thực hiện action
     * 
     * 🔄 Logic xử lý modal:
     * 1. MERGE MODAL: Lấy danh sách bàn OCCUPIED để gộp
     * 2. SPLIT MODAL: Validate bàn nguồn OCCUPIED, chuẩn bị danh sách bàn đích và
     * món
     * 
     * 📊 Data truyền cho view:
     * - tables: Danh sách tất cả bàn
     * - reservation: Object rỗng cho form đặt bàn
     * - showReservationForm: Flag hiển thị form đặt bàn
     * - Các data cho modal gộp/tách bàn nếu có
     * 
     * ⚠️ Error Handling:
     * - Validate trạng thái bàn nguồn cho tách bàn
     * - Kiểm tra tồn tại reservation và invoice
     * - Hiển thị thông báo lỗi chi tiết cho user
     */
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

        // ==========================================
        // BƯỚC 2: XỬ LÝ HIỂN THỊ MODAL GỘEP BÀN
        // ==========================================
        if (showMergeModal != null && showMergeModal) {
            // Lấy danh sách các bàn OCCUPIED để hiển thị trong modal gộp bàn
            var occupiedTables = tableRepository.findAll().stream()
                    .filter(table -> table.getStatus() == TableStatus.OCCUPIED)
                    .toList();

            model.addAttribute("showMergeModal", true);
            model.addAttribute("occupiedTables", occupiedTables);
            model.addAttribute("selectedTableId", selectedTableId);
        }

        // ==========================================
        // BƯỚC 3: XỬ LÝ HIỂN THỊ MODAL TÁCH BÀN
        // ==========================================
        if (showSplitModal != null && showSplitModal && selectedTableId != null) {
            try {
                // Sub-step 3.1: VALIDATE THÔNG TIN BÀN NGUỒN
                var sourceTableOpt = tableRepository.findById(selectedTableId);
                if (sourceTableOpt.isEmpty()) {
                    model.addAttribute("errorMessage", "Không tìm thấy bàn nguồn với ID: " + selectedTableId);
                    return "sales/sales";
                }

                var sourceTable = sourceTableOpt.get();
                // Sub-step 3.2: KIỂM TRA TRẠNG THÁI BÀN NGUỒN - chỉ tách được từ bàn OCCUPIED
                if (sourceTable.getStatus() != TableStatus.OCCUPIED) {
                    model.addAttribute("errorMessage",
                            "Chỉ có thể tách từ bàn đang sử dụng (OCCUPIED). Bàn hiện tại: " + sourceTable.getStatus());
                    return "sales/sales";
                }

                // Sub-step 3.3: KIỂM TRA RESERVATION VÀ INVOICE CỦA BÀN NGUỒN
                var sourceReservation = reservationService.findCurrentReservationByTableId(selectedTableId);
                if (sourceReservation == null) {
                    model.addAttribute("errorMessage", "Không tìm thấy thông tin đặt bàn cho bàn nguồn");
                    return "sales/sales";
                }

                if (sourceReservation.getInvoice() == null) {
                    model.addAttribute("errorMessage", "Không tìm thấy hóa đơn cho bàn nguồn");
                    return "sales/sales";
                }

                // Sub-step 3.4: LẤY DANH SÁCH CÁC MÓN CÓ THỂ TÁCH
                var invoiceDetails = invoiceDetailRepository
                        .findAllByInvoice_IdAndIsDeletedFalse(sourceReservation.getInvoice().getId());

                if (invoiceDetails.isEmpty()) {
                    model.addAttribute("errorMessage", "Bàn nguồn không có món nào để tách");
                    return "sales/sales";
                }

                // Sub-step 3.5: LẤY DANH SÁCH BÀN ĐÍCH KHẢ DỤNG
                // Bàn trống (AVAILABLE) - sẽ tạo hóa đơn mới
                var availableTables = tableRepository.findAll().stream()
                        .filter(table -> table.getStatus() == TableStatus.AVAILABLE)
                        .toList();

                // Bàn đang sử dụng (OCCUPIED) trừ bàn nguồn - sẽ cộng dồn vào hóa đơn hiện tại
                var occupiedTables = tableRepository.findAll().stream()
                        .filter(table -> table.getStatus() == TableStatus.OCCUPIED
                                && !table.getId().equals(selectedTableId))
                        .toList();

                // Sub-step 3.6: KIỂM TRA CÓ BÀN ĐÍCH KHẢ DỤNG KHÔNG
                if (availableTables.isEmpty() && occupiedTables.isEmpty()) {
                    System.out.println("ERROR: No target tables available");
                    model.addAttribute("errorMessage", "Không có bàn nào khả dụng để tách đến");
                    return "sales/sales";
                }

                // Sub-step 3.7: TẠO OBJECT REQUEST CHO FORM BINDING
                SplitTableRequest splitRequest = new SplitTableRequest();
                splitRequest.setSourceTableId(selectedTableId);
                splitRequest.setItems(new ArrayList<>()); // Khởi tạo danh sách items rỗng để tránh null pointer

                // Sub-step 3.8: TRUYỀN DỮ LIỆU CHO VIEW
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
        } // ==========================================
          // BƯỚC 4: XỬ LÝ HIỂN THỊ MODAL CHUYỂN BÀN
          // ==========================================
        if (showMoveModal != null && showMoveModal && selectedTableId != null) {
            try {
                // Sub-step 4.1: VALIDATE THÔNG TIN BÀN NGUỒN
                var sourceTableOpt = tableRepository.findById(selectedTableId);
                if (sourceTableOpt.isEmpty()) {
                    model.addAttribute("errorMessage", "Không tìm thấy bàn nguồn với ID: " + selectedTableId);
                    return "sales/sales";
                }

                var sourceTable = sourceTableOpt.get();
                // Sub-step 4.2: KIỂM TRA TRẠNG THÁI BÀN NGUỒN - chỉ chuyển được từ bàn OCCUPIED
                if (sourceTable.getStatus() != TableStatus.OCCUPIED) {
                    model.addAttribute("errorMessage",
                            "Chỉ có thể chuyển từ bàn đang sử dụng (OCCUPIED). Bàn hiện tại: "
                                    + sourceTable.getStatus());
                    return "sales/sales";
                }

                // Sub-step 4.3: KIỂM TRA RESERVATION CỦA BÀN NGUỒN
                var sourceReservation = reservationService.findCurrentReservationByTableId(selectedTableId);
                if (sourceReservation == null) {
                    model.addAttribute("errorMessage", "Không tìm thấy thông tin đặt bàn cho bàn nguồn");
                    return "sales/sales";
                }

                // Sub-step 4.4: LẤY DANH SÁCH BÀN ĐÍCH KHẢ DỤNG (chỉ bàn AVAILABLE)
                var allTables = tableRepository.findAll();
                var availableTables = allTables.stream()
                        .filter(table -> table.getStatus() == TableStatus.AVAILABLE)
                        .toList();

                if (availableTables.isEmpty()) {
                    model.addAttribute("errorMessage", "Không có bàn trống nào để chuyển đến");
                    return "sales/sales";
                }

                // Sub-step 4.5: TRUYỀN DỮ LIỆU CHO VIEW
                model.addAttribute("showMoveModal", true);
                model.addAttribute("selectedTableId", selectedTableId);
                model.addAttribute("tables", allTables);
                model.addAttribute("sourceTable", sourceTable);
                model.addAttribute("availableTables", availableTables);

                // Thêm các object mặc định để tránh lỗi template
                model.addAttribute("selectMenuRequest", new CreateSelectMenuRequest());

            } catch (Exception e) {
                model.addAttribute("errorMessage", "Lỗi khi thiết lập form chuyển bàn: " + e.getMessage());
                return "sales/sales";
            }
        }

        // ======================================
        // BƯỚC 5: THÊM CÁC OBJECT MẶC ĐỊNH CHO TEMPLATE
        // ======================================
        // Đảm bảo các object này luôn tồn tại để tránh lỗi template
        if (!model.containsAttribute("selectMenuRequest")) {
            model.addAttribute("selectMenuRequest", new CreateSelectMenuRequest());
        }
        if (!model.containsAttribute("reservation")) {
            model.addAttribute("reservation", new CreateReservationRequest());
        }

        // ======================================
        // BƯỚC 6: TRẢ VỀ VIEW SALES.HTML
        // ======================================
        return "sales/sales";
    }

    /**
     * ====== XÁC NHẬN THANH TOÁN (PAY INVOICE) ======
     * Endpoint: POST /sale/pay-invoice
     * Nhận vào tableId, cập nhật trạng thái hóa đơn, reservation, bàn khi thanh
     * toán.
     * Không lưu số tiền khách đưa/thối lại (xử lý ở frontend).
     * Chỉ cập nhật trạng thái: Invoice -> PAID, Reservation -> COMPLETED, Table ->
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
    // CHỨC NĂNG 4: ĐẶT BÀN (RESERVATION)
    // ================================================================================================

    /**
     * 📋 HIỂN THỊ TRANG ĐẶT BÀN RIÊNG BIỆT (Ít sử dụng)
     * 
     * Endpoint: GET /sale/reservation?tableId={id}
     * 
     * 🎯 Mục đích: Hiển thị trang đặt bàn riêng biệt (không phải popup)
     * 
     * 📋 Quy trình:
     * 1. Tạo ReservationRequest với tableId nếu có
     * 2. Truyền object cho view binding
     * 3. Trả về template reservation.html
     * 
     * ⚠️ Lưu ý: Method này ít được sử dụng, chủ yếu dùng showReservationForm()
     */
    @GetMapping("/reservation")
    public String getReservationPage(
            @RequestParam(required = false) Integer tableId,
            Model model) {
        // BƯỚC 1: Tạo reservation request với tableId nếu có
        CreateReservationRequest reservation = new CreateReservationRequest();
        if (tableId != null) {
            reservation.setTableId(tableId);
        }

        // BƯỚC 2: Truyền object cho view binding
        model.addAttribute("reservation", reservation);

        // BƯỚC 3: Trả về trang đặt bàn riêng biệt
        return "sales/reservation";
    }

    /**
     * 🎯 HIỂN THỊ FORM ĐẶT BÀN TRONG POPUP (Flow chính)
     * 
     * Endpoint: GET /sale/show-reservation-form?tableId={id}
     * 
     * 🎯 Mục đích: Hiển thị form đặt bàn dạng popup trong trang sales.html
     * 
     * 📋 Quy trình:
     * 1. Tạo ReservationRequest với tableId được chọn
     * 2. Set flag showReservationForm = true để hiển thị popup
     * 3. Truyền danh sách bàn và form object cho view
     * 4. Trả về sales.html với popup đặt bàn hiển thị
     * 
     * 🔄 Trạng thái bàn hỗ trợ: AVAILABLE (chỉ đặt được bàn trống)
     */
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

    /**
     * 💾 XỬ LÝ SUBMIT FORM ĐẶT BÀN (Core Business Logic)
     * 
     * Endpoint: POST /sale/reservations
     * 
     * 🎯 Mục đích: Xử lý submit form đặt bàn với validation và business logic
     * 
     * 📋 Quy trình xử lý:
     * 1. VALIDATION LAYER 1: Spring Framework Validation (@Valid annotation)
     * 2. SECURITY: Lấy thông tin nhân viên từ Security Context
     * 3. BUSINESS LOGIC: Gọi service tạo đặt bàn
     * 4. SUCCESS: Redirect về trang chính với thông báo thành công
     * 5. ERROR: Trả về form với thông báo lỗi cụ thể
     * 
     * 🔄 Trạng thái bàn:
     * - AVAILABLE → RESERVED (tạo reservation + invoice rỗng)
     * 
     * 🛡️ Error Handling:
     * - IllegalArgumentException: Lỗi nghiệp vụ (bàn không khả dụng, thông tin
     * không hợp lệ)
     * - RuntimeException: Lỗi hệ thống (log + thông báo generic)
     * 
     * ⚡ Performance Notes:
     * - Sử dụng @Transactional trong service để đảm bảo data consistency
     * - Validation nghiệp vụ: chỉ đặt được bàn AVAILABLE
     */
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
    // CHỨC NĂNG 5: HỦY BÀN (CANCEL RESERVATION)
    // ================================================================================================

    /**
     * 🎯 HIỂN THỊ FORM XÁC NHẬN HỦY BÀN
     * 
     * Endpoint: GET /sale/show-cancel-reservation-form?tableId={id}
     * 
     * 🎯 Mục đích: Hiển thị form xác nhận hủy bàn trong popup
     * 
     * 📋 Quy trình:
     * 1. Validate tableId và lấy thông tin bàn
     * 2. Tìm reservation hiện tại của bàn
     * 3. Validate business rules: chỉ hủy được bàn RESERVED
     * 4. Hiển thị form xác nhận hủy bàn trong popup
     * 
     * 🔄 Trạng thái bàn hỗ trợ: RESERVED (chỉ hủy được bàn đã đặt nhưng chưa chọn
     * món)
     * 
     * 🛡️ Business Rules:
     * - Không thể hủy bàn AVAILABLE (chưa đặt)
     * - Không thể hủy bàn OCCUPIED (đã chọn món)
     * - Chỉ hủy được reservation chưa bị soft delete
     */
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

    /**
     * Xử lý xác nhận hủy bàn (xóa mềm reservation, invoice, invoice detail, cập
     * nhật trạng thái bàn)
     */
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

    /**
     * ====== XỬ LÝ TÁCH BÀN (POST METHOD) ======
     * 
     * Logic tách bàn:
     * 1. Validate input từ form (Spring Validation + Business Logic)
     * 2. Lấy thông tin nhân viên thực hiện
     * 3. Gọi service thực hiện nghiệp vụ tách bàn
     * 4. Redirect về trang chính nếu thành công
     * 5. Trả về form với lỗi nếu thất bại
     */
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

    /**
     * ====== XỬ LÝ CHUYỂN BÀN (MOVE TABLE) ======
     * Endpoint: POST /sale/move-table
     * Chỉ cho phép chuyển từ bàn OCCUPIED sang bàn AVAILABLE.
     * Copy toàn bộ reservation, invoice, invoice details từ bàn nguồn sang bàn
     * đích.
     * Đổi trạng thái bàn đích thành OCCUPIED, bàn nguồn thành AVAILABLE.
     * Xóa mềm reservation ở bàn nguồn hoặc cập nhật lại cho đúng.
     */
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

}
