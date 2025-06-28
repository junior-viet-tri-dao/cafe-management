package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.request.export.ExportCreateRequest;
import com.viettridao.cafe.dto.request.export.ExportUpdateRequest;
import com.viettridao.cafe.dto.request.imports.ImportCreateRequest;
import com.viettridao.cafe.dto.request.imports.ImportUpdateRequest;
import com.viettridao.cafe.model.EmployeeEntity;
import com.viettridao.cafe.repository.EmployeeRepository;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.service.exports.IExportService;
import com.viettridao.cafe.service.imports.IImportService;
import com.viettridao.cafe.service.inventory.IInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/inventory")
public class InventoryController {

    private final IInventoryService iInventoryService;
    private final EmployeeRepository employeeRepository;
    private final ProductRepository productRepository;
    private final IImportService importService;
    private final IExportService exportService;

    @GetMapping
    public String listInventory(Model model) {
        model.addAttribute("transactions", iInventoryService.getAllTransactions());
        return "inventory/list";
    }

    @GetMapping("/import/new")
    public String showImportForm(Model model, @RequestParam(value = "productId", required = false) Integer productId) {
        ImportCreateRequest importRequest = new ImportCreateRequest();
        if (productId != null) {
            importRequest.setProductId(productId);
        }
        model.addAttribute("importRequest", importRequest);
        model.addAttribute("products", productRepository.findProductByDeletedFalse());
        return "inventory/import-inventory";
    }

    @PostMapping("/import")
    public String createImport(@ModelAttribute ImportCreateRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Integer employeeId = employeeRepository.findByAccount_Username(username)
                .map(EmployeeEntity::getId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với username: " + username));
        request.setEmployeeId(employeeId);
        importService.createImport(request);
        return "redirect:/inventory";
    }

    @GetMapping("/export/new")
    public String showExportForm(Model model, @RequestParam(value = "productId", required = false) Integer productId) {
        ExportCreateRequest exportRequest = new ExportCreateRequest();
        if (productId != null) {
            exportRequest.setProductId(productId);
        }
        model.addAttribute("exportRequest", exportRequest);
        model.addAttribute("products", productRepository.findProductByDeletedFalse());
        return "inventory/export-inventory";
    }

    @PostMapping("/export")
    public String createExport(@ModelAttribute ExportCreateRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Integer employeeId = employeeRepository.findByAccount_Username(username)
                .map(EmployeeEntity::getId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên với username: " + username));
        request.setEmployeeId(employeeId);
        exportService.createExport(request);
        return "redirect:/inventory";
    }

    @GetMapping("import/edit/{id}")
    public String showEditImportForm(@PathVariable Integer id, Model model) {
        if (id == null) {
            model.addAttribute("error", "Import ID must not be null");
            model.addAttribute("import", new ImportUpdateRequest());
            model.addAttribute("products", productRepository.findProductByDeletedFalse());
            return "inventory/form-import-edit";
        }
        try {
            ImportUpdateRequest updateRequest = importService.getUpdateForm(id);
            model.addAttribute("import", updateRequest);
            model.addAttribute("importId", id);
            model.addAttribute("products", productRepository.findProductByDeletedFalse());
            return "inventory/form-import-edit";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("import", new ImportUpdateRequest());
            model.addAttribute("products", productRepository.findProductByDeletedFalse());
            return "inventory/form-import-edit";
        }
    }

    @PostMapping("import/{id}")
    public String updateImport(@PathVariable Integer id, @ModelAttribute("import") ImportUpdateRequest request, Model model) {
        if (id == null) {
            model.addAttribute("error", "Import ID must not be null");
            model.addAttribute("products", productRepository.findProductByDeletedFalse());
            return "inventory/form-import-edit";
        }
        try {
            importService.updateImport(id, request);
            return "redirect:/inventory";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("products", productRepository.findProductByDeletedFalse());
            return "inventory/form-import-edit";
        }
    }

    @GetMapping("export/edit/{id}")
    public String showEditExportForm(@PathVariable Integer id, Model model) {
        System.out.println("Showing edit form for export ID: " + id);
        if (id == null) {
            model.addAttribute("error", "Export ID must not be null");
            model.addAttribute("export", new ExportUpdateRequest());
            model.addAttribute("products", productRepository.findProductByDeletedFalse());
            return "inventory/form-export-edit";
        }
        try {
            ExportUpdateRequest updateRequest = exportService.getUpdateForm(id);
            System.out.println("Export Date: " + updateRequest.getExportDate());
            model.addAttribute("export", updateRequest);
            model.addAttribute("exportId", id);
            model.addAttribute("products", productRepository.findProductByDeletedFalse());
            return "inventory/form-export-edit";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("export", new ExportUpdateRequest());
            model.addAttribute("products", productRepository.findProductByDeletedFalse());
            return "inventory/form-export-edit";
        }
    }

    @PostMapping("export/{id}")
    public String updateExport(@PathVariable Integer id, @ModelAttribute("export") ExportUpdateRequest request, Model model) {
        if (id == null) {
            model.addAttribute("error", "Export ID must not be null");
            model.addAttribute("products", productRepository.findProductByDeletedFalse());
            return "inventory/form-export-edit";
        }
        try {
            exportService.updateExport(id, request);
            return "redirect:/inventory";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("products", productRepository.findProductByDeletedFalse());
            return "inventory/form-export-edit";
        }
    }

    @PostMapping("import/delete/{id}")
    public String deleteImport(@PathVariable Integer id) {
        importService.deleteImport(id);
        return "redirect:/inventory";
    }

    @PostMapping("export/delete/{id}")
    public String deleteExport(@PathVariable Integer id) {
        exportService.deleteExport(id);
        return "redirect:/inventory";
    }
}