package com.viettridao.cafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.dto.request.export.CreateExportRequest;
import com.viettridao.cafe.dto.request.export.UpdateExportRequest;
import com.viettridao.cafe.dto.request.imports.CreateImportRequest;
import com.viettridao.cafe.dto.request.imports.UpdateImportRequest;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.service.ExportService;
import com.viettridao.cafe.service.ImportService;
import com.viettridao.cafe.service.ProductService;
import com.viettridao.cafe.service.WarehouseTransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/warehouse/transaction")
public class WarehouseTransactionController {

    private final WarehouseTransactionService warehouseTransactionService;
    private final EmployeeRepository employeeRepository;
    private final ProductRepository productRepository;
    private final ImportService importService;
    private final ExportService exportService;
    private final ProductService productService;

    // ---------- 1. Hiển thị toàn bộ danh sách giao dịch nhập xuất ----------
    @GetMapping("")
    public String getAllTransactions(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        model.addAttribute("transactions",
                warehouseTransactionService.getTransactions(keyword, page, size));
        return "/warehouses/warehouse";

    }

    // ---------- 2. Đơn nhập ----------
    @GetMapping("/import/create")
    public String showFormCreateImport(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("import", new CreateImportRequest());
        return "/warehouses/create_warehouse_import";
    }

    @PostMapping("/import/create")
    public String createImport(@Valid @ModelAttribute("import") CreateImportRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("products", productService.getAllProducts());
            return "/warehouses/create_warehouse_import";
        }
        try {
            importService.createImport(request);
            redirectAttributes.addFlashAttribute("success", "Thêm đơn nhập thành công");
            return "redirect:/warehouse/transaction";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/warehouse/create_warehouse_import";
        }
    }

    @GetMapping("/import/update/{id}")
    public String showFormUpdateImport(@PathVariable("id") Integer id, Model model, RedirectAttributes redirect) {
        try {
            UpdateImportRequest updateRequest = importService.getUpdateForm(id);
            model.addAttribute("import", updateRequest);
            model.addAttribute("products", productService.getAllProducts());
            return "/warehouses/update_warehouse_import";
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Không tìm thấy đơn nhập");
            return "redirect:/warehouse/transaction";
        }
    }

    @PostMapping("/import/update")
    public String updateImport(
            @Valid @ModelAttribute("import") UpdateImportRequest request,
            BindingResult result,
            RedirectAttributes redirect,
            Model model) {

        // Kiểm tra lỗi validate từ @Valid
        if (result.hasErrors()) {
            // Gửi lại danh sách sản phẩm để hiển thị lại form nếu lỗi
            model.addAttribute("products", productService.getAllProducts());
            return "/warehouses/update_warehouse_import";
        }

        try {
            importService.updateImport(request.getId(), request);  // Gọi Service để cập nhật đơn nhập
            redirect.addFlashAttribute("success", "Cập nhật đơn nhập thành công!");
            return "redirect:/warehouse/transaction";
        } catch (Exception e) {
            // Nếu có lỗi, báo lại và giữ nguyên form
            model.addAttribute("error", e.getMessage());
            model.addAttribute("products", productService.getAllProducts());
            return "/warehouses/update_warehouse_import";
        }
    }

    @PostMapping("/import/delete/{id}")
    public String deleteImport(@PathVariable("id") Integer id,
            RedirectAttributes redirectAttributes) {
        try {
            importService.deleteImport(id);
            redirectAttributes.addFlashAttribute("success", "Xóa đơn nhập thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/warehouse/transaction";
    }

    // ---------- 3. Đơn xuất ----------

    @GetMapping("/export/create")
    public String showFormCreateExport(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("export", new CreateExportRequest());
        return "/warehouses/create_warehouse_export";
    }

    @PostMapping("/export/create")
    public String createExport(@Valid @ModelAttribute("export") CreateExportRequest request,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if (result.hasErrors()) {
            model.addAttribute("products", productService.getAllProducts());
            return "/warehouses/create_warehouse_export";
        }

        try {
            exportService.createExport(request);
            redirectAttributes.addFlashAttribute("success", "Thêm đơn xuất thành công");
            return "redirect:/warehouse/transaction";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/warehouse/create_warehouse_export";
        }
    }

    @GetMapping("/export/update/{id}")
    public String showFormUpdateExport(@PathVariable("id") Integer id, Model model, RedirectAttributes redirect) {
        try {
            UpdateExportRequest updateRequest = exportService.getUpdateForm(id);
            model.addAttribute("export", updateRequest);
            model.addAttribute("products", productService.getAllProducts());
            return "/warehouses/update_warehouse_export";
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Không tìm thấy đơn xuất");
            return "redirect:/warehouse/transaction";
        }
    }

    @PostMapping("/export/update")
    public String updateExport(@Valid @ModelAttribute("export") UpdateExportRequest request,
                               BindingResult result,
                               RedirectAttributes redirect,
                               Model model) {
        if (result.hasErrors()) {
            model.addAttribute("products", productService.getAllProducts());
            return "/warehouses/update_warehouse_export";
        }

        try {
            exportService.updateExport(request.getId(), request);
            redirect.addFlashAttribute("success", "Cập nhật đơn xuất thành công");
            return "redirect:/warehouse/transaction";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("products", productService.getAllProducts());
            return "/warehouses/update_warehouse_export";
        }
    }

    @PostMapping("/export/delete/{id}")
    public String deleteExport(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            exportService.deleteExport(id);
            redirectAttributes.addFlashAttribute("success", "Xóa đơn xuất thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/warehouse/transaction";
    }


}
