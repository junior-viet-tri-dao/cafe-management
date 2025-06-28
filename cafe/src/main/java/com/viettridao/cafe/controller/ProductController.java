package com.viettridao.cafe.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.dto.request.product.ProductCreateRequest;
import com.viettridao.cafe.dto.request.product.ProductUpdateRequest;
import com.viettridao.cafe.dto.response.product.ProductResponse;
import com.viettridao.cafe.model.UnitEntity;
import com.viettridao.cafe.repository.UnitRepository;
import com.viettridao.cafe.service.product.IProductService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final IProductService productService;
    private final UnitRepository unitRepository;

    @GetMapping
    public String listProduct(Model model) {
        model.addAttribute("products", productService.getProductAll());
        return "product/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new ProductCreateRequest());
        return "product/form-create";
    }

    @PostMapping
    public String createProduct(@ModelAttribute ProductCreateRequest request,
                                @RequestParam(required = false) boolean redirectToImport,
                                RedirectAttributes redirectAttributes) {
        ProductResponse saved = productService.createProduct(request);

        if (redirectToImport) {
            return "redirect:/import/new?productId=" + saved.getId();
        }
        return "redirect:/product";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {

        ProductUpdateRequest updateRequest = productService.getUpdateForm(id);
        model.addAttribute("product", updateRequest);
        model.addAttribute("productId", id);
        List<UnitEntity> units = unitRepository.findByDeletedFalse(); // bạn cần tạo method này
        model.addAttribute("units", units);
        return "product/form-edit";
    }

    @PostMapping("/{id}")
    public String updateProduct(@PathVariable Integer id, @ModelAttribute("product") ProductUpdateRequest request) {
        productService.updateProduct(id, request);
        return "redirect:/product";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return "redirect:/product";
    }
}
