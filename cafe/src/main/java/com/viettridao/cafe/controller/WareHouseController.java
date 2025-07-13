package com.viettridao.cafe.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.dto.request.export.ExportRequest;
import com.viettridao.cafe.dto.request.imports.ImportRequest;
import com.viettridao.cafe.dto.request.product.ProductRequest;
import com.viettridao.cafe.dto.response.product.ProductResponse;
import com.viettridao.cafe.service.ExportService;
import com.viettridao.cafe.service.ImportService;
import com.viettridao.cafe.service.ProductService;
import com.viettridao.cafe.service.UnitService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/warehouse")
@RequiredArgsConstructor
public class WarehouseController {

	private final ProductService productService;
	private final ImportService importService;
	private final ExportService exportService;
	private final UnitService unitService;

	@GetMapping
	public String listProducts(@RequestParam(defaultValue = "0") int page,
	                           @RequestParam(defaultValue = "10") int size,
	                           Model model) {
		Page<ProductResponse> productPage = productService.findAllPaged(page, size);
		model.addAttribute("productPage", productPage);
		return "warehouse/list";
	}

	@GetMapping("/import")
	public String showImportForm(Model model) {
		model.addAttribute("importRequest", new ImportRequest());
		model.addAttribute("units", unitService.findAll());
		return "warehouse/import-form";
	}

	@PostMapping("/import")
	public String handleImport(@Valid @ModelAttribute("importRequest") ImportRequest request,
	                           BindingResult result,
	                           Model model,
	                           RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("units", unitService.findAll());
			return "warehouse/import-form";
		}

		try {
			importService.createImport(request);
			redirectAttributes.addFlashAttribute("success", "Nhập hàng thành công!");
			return "redirect:/warehouse";
		} catch (RuntimeException e) {
			model.addAttribute("units", unitService.findAll());
			model.addAttribute("error", "Nhập hàng thất bại: " + e.getMessage());
			return "warehouse/import-form";
		}
	}

	@GetMapping("/export")
	public String showExportForm(Model model) {
		model.addAttribute("exportRequest", new ExportRequest());
		model.addAttribute("products", productService.findAll());
		return "warehouse/export-form";
	}

	@PostMapping("/export")
	public String handleExport(@Valid @ModelAttribute("exportRequest") ExportRequest request,
	                           BindingResult result,
	                           Model model,
	                           RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("products", productService.findAll());
			return "warehouse/export-form";
		}

		try {
			exportService.createExport(request);
			redirectAttributes.addFlashAttribute("success", "Xuất hàng thành công!");
			return "redirect:/warehouse";
		} catch (RuntimeException e) {
			model.addAttribute("products", productService.findAll());
			model.addAttribute("error", "Xuất hàng thất bại: " + e.getMessage());
			return "warehouse/export-form";
		}
	}

	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Integer id, Model model) {
		try {
			ProductResponse product = productService.findById(id);
			if (product == null) {
				model.addAttribute("error", "Không tìm thấy sản phẩm.");
				return "redirect:/warehouse";
			}

			ProductRequest request = new ProductRequest();
			request.setProductName(product.getProductName());
			request.setUnitId(product.getUnitId());
			request.setQuantity(product.getCurrentQuantity());
			request.setPrice(product.getLatestPrice() != null ? product.getLatestPrice().doubleValue() : 0.0);
			request.setImportDate(product.getLastImportDate());

			model.addAttribute("productRequest", request);
			model.addAttribute("productId", id);
			model.addAttribute("units", unitService.findAll());
			return "warehouse/edit-form";
		} catch (Exception e) {
			model.addAttribute("error", "Có lỗi xảy ra khi tải sản phẩm.");
			return "redirect:/warehouse";
		}
	}

	@PostMapping("/edit/{id}")
	public String updateProduct(@PathVariable Integer id,
	                            @Valid @ModelAttribute("productRequest") ProductRequest request,
	                            BindingResult result,
	                            Model model,
	                            RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("productId", id);
			model.addAttribute("units", unitService.findAll());
			return "warehouse/edit-form";
		}

		try {
			productService.update(id, request);
			redirectAttributes.addFlashAttribute("success", "Cập nhật sản phẩm thành công!");
			return "redirect:/warehouse";
		} catch (RuntimeException e) {
			model.addAttribute("productId", id);
			model.addAttribute("units", unitService.findAll());
			model.addAttribute("error", "Cập nhật thất bại: " + e.getMessage());
			return "warehouse/edit-form";
		}
	}

	@GetMapping("/search")
	public String search(@RequestParam String keyword,
	                     @RequestParam(defaultValue = "0") int page,
	                     @RequestParam(defaultValue = "10") int size,
	                     Model model) {
		Page<ProductResponse> productPage = productService.search(keyword, page, size);
		model.addAttribute("productPage", productPage);
		model.addAttribute("keyword", keyword);
		return "warehouse/list";
	}

	@GetMapping("/stock/{productId}")
	@ResponseBody
	public String getStock(@PathVariable Integer productId) {
		int stock = productService.getCurrentStock(productId);
		return "Tồn kho hiện tại: " + stock;
	}

	@GetMapping("/delete/{id}")
	public String softDelete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		productService.delete(id);
		redirectAttributes.addFlashAttribute("success", "Xóa sản phẩm thành công!");
		return "redirect:/warehouse";
	}

	@GetMapping("/history/import")
	public String viewAllImportHistory(Model model) {
		model.addAttribute("imports", importService.getAll());
		return "warehouse/import-history";
	}

	@GetMapping("/history/export")
	public String viewAllExportHistory(Model model) {
		model.addAttribute("exports", exportService.getAll());
		return "warehouse/export-history";
	}
}
