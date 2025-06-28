package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.request.equipment.UpdateEquipmentRequest;
import com.viettridao.cafe.dto.request.export.CreateExportRequest;
import com.viettridao.cafe.dto.request.imports.CreateImportRequest;
import com.viettridao.cafe.dto.request.imports.UpdateImportRequest;
import com.viettridao.cafe.mapper.ImportMapper;
import com.viettridao.cafe.mapper.ProductMapper;
import com.viettridao.cafe.service.ExportService;
import com.viettridao.cafe.service.ImportService;
import com.viettridao.cafe.service.ProductService;
import com.viettridao.cafe.service.WareHouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/warehouse")
public class WareHouseController {
    private final WareHouseService wareHouseService;
    private final ImportService importService;
    private final ExportService exportService;
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final ImportMapper importMapper;

    @GetMapping("")
    public String home(@RequestParam(required = false) String keyword,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "5") int size,
                       Model model) {
        model.addAttribute("warehouses", wareHouseService.getAllWareHouses(keyword, page, size));
        return "/warehouses/warehouse";
    }

    @GetMapping("/create")
    public String showFormCreate(Model model) {
        model.addAttribute("products", productMapper.toProductResponse(productService.getAllProducts()));
        model.addAttribute("import", new CreateImportRequest());
        return "/warehouses/create_warehouse_import";
    }

    @PostMapping("/create")
    public String createWareHouse(@Valid @ModelAttribute("import") CreateImportRequest request, BindingResult result, RedirectAttributes redirectAttributes) {
        try{
            if (result.hasErrors()) {
                return "/warehouses/create_warehouse_import";
            }
            importService.createImport(request);
            redirectAttributes.addFlashAttribute("success", "Thêm đơn nhập thành công");
            return "redirect:/warehouse";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/warehouse/create";
        }
    }

    @GetMapping("/create/export")
    public String showFormCreateExport(Model model) {
        model.addAttribute("products", productMapper.toProductResponse(productService.getAllProducts()));
        model.addAttribute("export", new CreateExportRequest());
        return "/warehouses/create_warehouse_export";
    }

    @PostMapping("/create/export")
    public String createWareHouseExport(@Valid @ModelAttribute("export") CreateExportRequest request, BindingResult result, RedirectAttributes redirectAttributes) {
        try{
            if (result.hasErrors()) {
                return "/warehouses/create_warehouse_export";
            }
            exportService.createExport(request);
            redirectAttributes.addFlashAttribute("success", "Thêm đơn xuất thành công");
            return "redirect:/warehouse";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/warehouse/create/export";
        }
    }

    @GetMapping("/update/{id}")
    public String showFormUpdate(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try{
            model.addAttribute("import", importMapper.toImportResponse(importService.getImportById(id)));
            model.addAttribute("products", productMapper.toProductResponse(productService.getAllProducts()));
            return "/warehouses/update_warehouse";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/warehouse";
        }
    }

    @PostMapping("/update")
    public String updateWareHouse(@Valid @ModelAttribute UpdateImportRequest request, BindingResult result, RedirectAttributes redirectAttributes) {
        try{
            if (result.hasErrors()) {
                return "/warehouses/update_warehouse";
            }
            importService.updateImport(request);
            redirectAttributes.addFlashAttribute("success", "Chỉnh sửa đơn nhập thành công");
            return "redirect:/warehouse";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/warehouse";
        }
    }

}
