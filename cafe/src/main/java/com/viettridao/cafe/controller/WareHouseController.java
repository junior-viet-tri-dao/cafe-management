package com.viettridao.cafe.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.dto.request.export.ExportRequest;
import com.viettridao.cafe.dto.request.imports.ImportRequest;
import com.viettridao.cafe.dto.request.product.ProductRequest;
import com.viettridao.cafe.dto.response.product.ProductResponse;
import com.viettridao.cafe.service.ExportService;
import com.viettridao.cafe.service.ImportService;
import com.viettridao.cafe.service.ProductService;
import com.viettridao.cafe.service.UnitService;

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
	public String viewProducts(@RequestParam(required = false) String keyword,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, Model model) {
		try {
			Page<ProductResponse> productPage;

			if (keyword != null && !keyword.trim().isEmpty()) {
				productPage = productService.search(keyword, page, size);
				model.addAttribute("keyword", keyword);
			} else {
				productPage = productService.getAllPaged(page, size);
			}

			model.addAttribute("productPage", productPage);
		} catch (Exception e) {
			model.addAttribute("error", "Lỗi khi tải danh sách hàng hóa: " + e.getMessage());
		}
		return "warehouse/list";
	}

	@GetMapping("/import")
	public String showImportForm(Model model) {
		try {
			model.addAttribute("importRequest", new ImportRequest());
			model.addAttribute("products", productService.getAll());
		} catch (Exception e) {
			model.addAttribute("error", "Lỗi khi hiển thị form nhập hàng: " + e.getMessage());
		}
		return "warehouse/import";
	}

	@PostMapping("/import")
	public String importProduct(@ModelAttribute("importRequest") ImportRequest request, RedirectAttributes redirect) {
		try {
			if (request.getEmployeeId() == null) {
				request.setEmployeeId(1);
			}
			importService.createImport(request);
			redirect.addFlashAttribute("success", "Nhập hàng thành công!");
		} catch (RuntimeException e) {
			redirect.addFlashAttribute("error", e.getMessage());
			return "redirect:/warehouse/import";
		}
		return "redirect:/warehouse";
	}

	@GetMapping("/export")
	public String showExportForm(Model model) {
		try {
			model.addAttribute("exportRequest", new ExportRequest());
			model.addAttribute("products", productService.getAll());
		} catch (Exception e) {
			model.addAttribute("error", "Lỗi khi hiển thị form xuất hàng: " + e.getMessage());
		}
		return "warehouse/export";
	}

	@PostMapping("/export")
	public String exportProduct(@ModelAttribute("exportRequest") ExportRequest request, RedirectAttributes redirect) {
		try {
			if (request.getEmployeeId() == null) {
				request.setEmployeeId(1);
			}
			exportService.createExport(request);
			redirect.addFlashAttribute("success", "Xuất hàng thành công!");
		} catch (RuntimeException e) {
			redirect.addFlashAttribute("error", e.getMessage());
			return "redirect:/warehouse/export";
		}
		return "redirect:/warehouse";
	}

	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirect) {
		try {
			ProductResponse response = productService.getById(id);

			ProductRequest request = new ProductRequest();
			request.setId(response.getId());
			request.setProductName(response.getProductName());
			request.setQuantity(response.getQuantity());
			request.setProductPrice(response.getProductPrice());
			request.setUnitId(response.getUnitId());
			request.setImportDate(response.getLatestImportDate());

			model.addAttribute("product", request);
			model.addAttribute("units", unitService.getAll());
		} catch (Exception e) {
			redirect.addFlashAttribute("error", "Không thể hiển thị thông tin chỉnh sửa: " + e.getMessage());
			return "redirect:/warehouse";
		}
		return "warehouse/edit";
	}

	@PostMapping("/edit")
	public String updateProduct(@ModelAttribute("product") ProductRequest request, RedirectAttributes redirect) {
		try {
			productService.update(request.getId(), request);
			redirect.addFlashAttribute("success", "Cập nhật thành công!");
		} catch (RuntimeException e) {
			redirect.addFlashAttribute("error", e.getMessage());
		}
		return "redirect:/warehouse";
	}
}
