package com.viettridao.cafe.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.viettridao.cafe.dto.request.export_request.ExportRequest;
import com.viettridao.cafe.dto.request.import_request.ImportRequest;
import com.viettridao.cafe.dto.response.product.ProductResponse;
import com.viettridao.cafe.service.ExportService;
import com.viettridao.cafe.service.ImportService;
import com.viettridao.cafe.service.ProductService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProductController extends BaseController {

    private final ProductService productService;
    private final ImportService importService; // Assuming you have an ImportService to handle import logic
    private final ExportService exportService; // Assuming you have an ExportService to handle export logic

    @GetMapping("/product")
    public String showProduct(Model model) {

        List<ProductResponse> products = productService.getAllProducts();

        model.addAttribute("listProduct", products);
        return "products/product";
    }

    @GetMapping("/product/import")
    public String showImportProduct(Model model) {

        List<ProductResponse> products = productService.getAllProducts();

        model.addAttribute("listProduct", products);
        model.addAttribute("importRequest", new ImportRequest()); // Placeholder for import request object
        return "products/product-import";
    }

    @PostMapping("/product/import")
    public String importProduct(@ModelAttribute ImportRequest importRequest, Model model, HttpSession session) {
        try {
            List<ProductResponse> products = productService.getAllProducts();

            // Sau khi thành công, load lại danh sách sản phẩm và thông báo
            model.addAttribute("listProduct", products);
            importService.createImport(importRequest, getCurrentUser(session).getEmployee().getId());
            model.addAttribute("successMessage", "Nhập hàng thành công!");

            return "products/product-import";
        } catch (Exception e) {
            // Nếu có lỗi, trả lại form với thông báo lỗi
            List<ProductResponse> products = productService.getAllProducts();
            model.addAttribute("listProduct", products);
            model.addAttribute("importRequest", importRequest);
            model.addAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            return "products/product-import";
        }
    }

    @GetMapping("/product/export")
    public String showExportProduct(Model model) {

        List<ProductResponse> products = productService.getAllProducts();

        model.addAttribute("listProduct", products);
        model.addAttribute("exportRequest", new ExportRequest());
        return "products/product-export";
    }

    @PostMapping("/product/export")
    public String exportProduct(
            @ModelAttribute("exportRequest") @Valid ExportRequest exportRequest,
            BindingResult bindingResult,
            Model model, HttpSession session) {
        List<ProductResponse> products = productService.getAllProducts();
        model.addAttribute("listProduct", products);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Vui lòng kiểm tra lại thông tin!");
            return "products/product-export";
        }
        try {
            var idEmployee = getCurrentUser(session).getEmployee().getId();
            exportService.createExport(exportRequest, idEmployee);
            model.addAttribute("successMessage", "Xuất hàng thành công!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
        }
        return "products/product-export";
    }
}
