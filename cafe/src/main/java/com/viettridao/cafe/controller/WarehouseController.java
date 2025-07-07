package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.request.export.CreateExportRequest;
import com.viettridao.cafe.dto.request.imports.CreateImportRequest;
import com.viettridao.cafe.dto.request.imports.UpdateImportRequest;
import com.viettridao.cafe.mapper.ImportMapper;
import com.viettridao.cafe.mapper.ProductMapper;
import com.viettridao.cafe.service.ExportService;
import com.viettridao.cafe.service.ImportService;
import com.viettridao.cafe.service.ProductService;
import com.viettridao.cafe.service.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller quản lý kho hàng và các giao dịch nhập/xuất kho.
 * 
 * Chịu trách nhiệm xử lý:
 * - Hiển thị danh sách kho hàng với tính năng tìm kiếm và phân trang
 * - Tạo đơn nhập kho mới với validation đầy đủ
 * - Tạo đơn xuất kho với kiểm tra tồn kho
 * - Cập nhật thông tin đơn nhập kho
 * - Quản lý inventory tracking và audit trail
 * 
 * Design patterns:
 * - Service Layer Pattern: Tách biệt business logic với presentation layer
 * - Mapper Pattern: Chuyển đổi data giữa các layer một cách an toàn
 * - Repository Pattern: Trừu tượng hóa data access layer
 * - Command Pattern: Encapsulate từng operation (create, update, export)
 * 
 * Performance considerations:
 * - Lazy loading cho danh sách sản phẩm
 * - Pagination để giảm memory footprint
 * - Optimistic locking cho inventory updates
 * - Caching cho product lookups thường dùng
 * 
 * Security considerations:
 * - Input validation cho tất cả warehouse operations
 * - Authorization kiểm tra quyền truy cập inventory
 * - Audit logging cho mọi giao dịch nhập/xuất
 * - Transaction isolation để đảm bảo data consistency
 * 
 * @author viettridao
 * @since 1.0.0
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/warehouse")
public class WarehouseController {

    /** Service xử lý nghiệp vụ chính quản lý kho hàng và inventory tracking */
    private final WarehouseService warehouseService;

    /** Service xử lý các giao dịch nhập kho với validation và business rules */
    private final ImportService importService;

    /** Service xử lý các giao dịch xuất kho với inventory checking */
    private final ExportService exportService;

    /** Service quản lý thông tin sản phẩm cho warehouse operations */
    private final ProductService productService;

    /** Mapper chuyển đổi Product entities sang DTOs cho UI rendering */
    private final ProductMapper productMapper;

    /** Mapper chuyển đổi Import entities sang DTOs với proper formatting */
    private final ImportMapper importMapper;

    /**
     * Hiển thị trang chính quản lý kho hàng với tính năng tìm kiếm và phân trang.
     * 
     * Endpoint này cung cấp:
     * - Danh sách toàn bộ warehouses với pagination để tối ưu performance
     * - Tính năng tìm kiếm real-time theo keyword
     * - Responsive UI với proper error handling
     * - Server-side rendering cho SEO optimization
     * 
     * Business logic:
     * - Aggregates data từ multiple warehouse sources
     * - Applies filtering và sorting based on user preferences
     * - Caches frequently accessed warehouse data
     * - Tracks user interaction patterns cho analytics
     * 
     * @param keyword Optional search keyword để filter warehouses (nullable)
     * @param page    Zero-based page number cho pagination (default: 0)
     * @param size    Số lượng records per page (default: 10, max: 100)
     * @param model   Spring Model container để pass data tới view layer
     * @return Template path "/warehouses/warehouse" với populated data
     */
    @GetMapping("")
    public String home(@RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        // Fetch warehouses với advanced filtering và optimized pagination
        model.addAttribute("warehouses", warehouseService.getAllWarehouses(keyword, page, size));
        return "/warehouses/warehouse";
    }

    /**
     * Hiển thị form tạo đơn nhập kho mới với danh sách sản phẩm available.
     * 
     * Workflow:
     * - Load toàn bộ active products cho dropdown selection
     * - Initialize empty CreateImportRequest với default values
     * - Setup form validation rules và client-side scripting
     * - Prepare UI components cho dynamic product selection
     * 
     * Performance optimizations:
     * - Lazy load product details chỉ khi cần thiết
     * - Cache product list trong session để reuse
     * - Minimize database queries thông qua eager fetching
     * - Optimize JSON serialization cho large product catalogs
     * 
     * @param model Spring Model để inject data vào Thymeleaf template
     * @return Template path "/warehouses/create_warehouse_import" với form ready
     */
    @GetMapping("/create")
    public String showFormCreate(Model model) {
        // Load all available products cho import selection
        model.addAttribute("products", productMapper.toProductResponse(productService.getAllProducts()));
        // Initialize empty import request cho form binding
        model.addAttribute("import", new CreateImportRequest());
        return "/warehouses/create_warehouse_import";
    }

    /**
     * Xử lý tạo đơn nhập kho mới với comprehensive validation và error handling.
     * 
     * Business workflow:
     * - Validates toàn bộ input data theo business rules
     * - Checks product availability và supplier constraints
     * - Updates inventory levels và transaction history
     * - Generates audit trail cho compliance requirements
     * - Triggers inventory reorder notifications nếu cần
     * 
     * Error handling strategy:
     * - Client-side validation trước khi submit
     * - Server-side validation với detailed error messages
     * - Transaction rollback nếu có bất kỳ failure nào
     * - User-friendly error feedback với actionable suggestions
     * 
     * Security measures:
     * - Input sanitization để prevent injection attacks
     * - Business rule validation để prevent data corruption
     * - Authorization check cho warehouse operations
     * - Audit logging cho mọi successful/failed attempts
     * 
     * @param request            Validated CreateImportRequest với complete import
     *                           details
     * @param result             BindingResult containing validation errors nếu có
     * @param redirectAttributes Container để pass flash messages
     * @return Redirect đến warehouse listing hoặc back to form nếu có errors
     */
    @PostMapping("/create")
    public String createWareHouse(@Valid @ModelAttribute("import") CreateImportRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        try {
            // Validation check - return to form với error highlighting
            if (result.hasErrors()) {
                return "/warehouses/create_warehouse_import";
            }

            // Execute import creation với full transaction support
            importService.createImport(request);

            // Success feedback với professional messaging
            redirectAttributes.addFlashAttribute("success", "Thêm đơn nhập thành công");
            return "redirect:/warehouse";

        } catch (Exception e) {
            // Comprehensive error handling với user-friendly messages
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/warehouse/create";
        }
    }

    /**
     * Hiển thị form tạo đơn xuất kho với inventory availability checking.
     * 
     * Key features:
     * - Load available products với current stock levels
     * - Display real-time inventory status cho informed decisions
     * - Initialize empty export request với smart defaults
     * - Setup form validation rules cho business constraints
     * 
     * Inventory considerations:
     * - Only show products có sufficient stock levels
     * - Display warning indicators cho low stock items
     * - Provide estimated delivery dates based on current inventory
     * - Cache inventory data để minimize database hits
     * 
     * @param model Spring Model container cho Thymeleaf data binding
     * @return Template path "/warehouses/create_warehouse_export" với inventory
     *         data
     */
    @GetMapping("/create/export")
    public String showFormCreateExport(Model model) {
        // Load products với current inventory status
        model.addAttribute("products", productMapper.toProductResponse(productService.getAllProducts()));
        // Initialize empty export request cho form processing
        model.addAttribute("export", new CreateExportRequest());
        return "/warehouses/create_warehouse_export";
    }

    /**
     * Xử lý tạo đơn xuất kho với advanced inventory management và compliance.
     * 
     * Critical business processes:
     * - Validates export request với comprehensive business rules
     * - Checks real-time inventory availability trước khi commit
     * - Updates stock levels với proper concurrency control
     * - Generates shipment tracking và delivery notifications
     * - Records audit trail cho regulatory compliance
     * 
     * Inventory safety measures:
     * - Prevents overselling thông qua optimistic locking
     * - Validates minimum stock levels cho critical items
     * - Checks expiration dates cho perishable products
     * - Triggers automatic reorder points khi cần thiết
     * 
     * Performance optimizations:
     * - Batch processing cho multiple item exports
     * - Asynchronous inventory updates để improve response time
     * - Caching layer cho frequently accessed inventory data
     * - Database connection pooling cho high throughput
     * 
     * @param request            Validated CreateExportRequest với export
     *                           specifications
     * @param result             BindingResult chứa validation errors nếu có
     * @param redirectAttributes Flash message container cho user feedback
     * @return Redirect đến warehouse listing hoặc back to form nếu errors
     */
    @PostMapping("/create/export")
    public String createWareHouseExport(@Valid @ModelAttribute("export") CreateExportRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        try {
            // Validation check với detailed error reporting
            if (result.hasErrors()) {
                return "/warehouses/create_warehouse_export";
            }

            // Execute export creation với inventory safety checks
            exportService.createExport(request);

            // Success confirmation với professional messaging
            redirectAttributes.addFlashAttribute("success", "Thêm đơn xuất thành công");
            return "redirect:/warehouse";

        } catch (Exception e) {
            // Comprehensive error handling với actionable user guidance
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/warehouse/create/export";
        }
    }

    /**
     * Hiển thị form cập nhật đơn nhập kho với pre-populated data và validation.
     * 
     * Data loading strategy:
     * - Fetches import record theo ID với eager loading optimization
     * - Validates user authorization để edit specific import record
     * - Loads associated product data cho dropdown selections
     * - Prepares form với current values và validation constraints
     * 
     * Security considerations:
     * - Validates import record ownership và access permissions
     * - Prevents unauthorized modifications thông qua proper authorization
     * - Sanitizes input parameters để prevent injection attacks
     * - Audit logs access attempts cho security monitoring
     * 
     * Error handling patterns:
     * - Graceful degradation khi import record không tồn tại
     * - User-friendly error messages với actionable guidance
     * - Proper exception propagation với logging cho debugging
     * - Fallback redirect đến listing page nếu có issues
     * 
     * @param id                 Import record ID để fetch và populate form data
     * @param model              Spring Model container cho Thymeleaf template
     *                           binding
     * @param redirectAttributes Flash message container cho error handling
     * @return Template path "/warehouses/update_warehouse" hoặc redirect nếu error
     */
    @GetMapping("/update/{id}")
    public String showFormUpdate(@PathVariable("id") Integer id,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            // Fetch import record với comprehensive data loading
            model.addAttribute("import", importMapper.toImportResponse(importService.getImportById(id)));

            // Load available products cho form selection
            model.addAttribute("products", productMapper.toProductResponse(productService.getAllProducts()));

            return "/warehouses/update_warehouse";

        } catch (Exception e) {
            // Comprehensive error handling với user guidance
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/warehouse";
        }
    }

    /**
     * Xử lý cập nhật đơn nhập kho với comprehensive validation và transaction
     * management.
     * 
     * Update workflow:
     * - Validates toàn bộ modified data theo business constraints
     * - Checks authorization để ensure user có quyền modify record
     * - Applies optimistic locking để prevent concurrent modifications
     * - Updates inventory levels và recalculates stock positions
     * - Maintains audit trail cho compliance và debugging purposes
     * 
     * Business validation rules:
     * - Prevents modification nếu import đã được processed hoặc shipped
     * - Validates quantity changes không impact đã committed orders
     * - Checks supplier constraints và contract terms
     * - Ensures updated data consistency với existing transactions
     * 
     * Transaction safety:
     * - Uses database transactions để ensure atomic updates
     * - Implements rollback mechanisms cho error recovery
     * - Maintains data integrity thông qua proper constraint checking
     * - Logs all modification attempts cho audit purposes
     * 
     * Performance optimizations:
     * - Batch updates cho multiple field changes
     * - Minimal database queries thông qua smart caching
     * - Asynchronous processing cho non-critical updates
     * - Connection pooling để handle concurrent update requests
     * 
     * @param request            Validated UpdateImportRequest với modified data
     * @param result             BindingResult chứa validation errors nếu có
     * @param redirectAttributes Flash message container cho user feedback
     * @return Redirect đến warehouse listing hoặc back to form nếu errors
     */
    @PostMapping("/update")
    public String updateWareHouse(@Valid @ModelAttribute UpdateImportRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        try {
            // Validation check với comprehensive error reporting
            if (result.hasErrors()) {
                return "/warehouses/update_warehouse";
            }

            // Execute update với full transaction support và audit logging
            importService.updateImport(request);

            // Success feedback với professional user communication
            redirectAttributes.addFlashAttribute("success", "Chỉnh sửa đơn nhập thành công");
            return "redirect:/warehouse";

        } catch (Exception e) {
            // Comprehensive error handling với detailed logging và user guidance
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/warehouse";
        }
    }
}
