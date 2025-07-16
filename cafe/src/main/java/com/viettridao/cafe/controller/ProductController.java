package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.request.product.CreateProductRequest;
import com.viettridao.cafe.dto.request.product.UpdateProductRequest;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.repository.UnitRepository;
import com.viettridao.cafe.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final UnitRepository unitRepository;


    @GetMapping("")
    public String show_list_product(@RequestParam(required = false) String keyword,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "5") int size,
                                    Model model    ) {
        model.addAttribute("products", productService.getAllProductPage(keyword,page,size));

        return "product/list_product";
    }

    @GetMapping("/insert")
    public String show_form_insert_product(Model model){

        model.addAttribute("product", new CreateProductRequest());
        model.addAttribute("units", unitRepository.findAll());
        return "product/insert_product";
    }

    @PostMapping("/insert")
    public  String create_product(@Valid @ModelAttribute("product") CreateProductRequest request, BindingResult result){
        if (result.hasErrors()){
            return "product/insert_product";
        }

        productService.createProduct(request);

        return "redirect:/product";
    }


    @GetMapping("/delete/{id}")
    public String delete_product(@PathVariable("id") Integer id){
        productRepository.deleteById(id);
        return "redirect:/product";
    }

    @GetMapping("/edit/{id}")
    public String show_form_edit_product(@PathVariable("id") Integer id , Model model){
        ProductEntity productEntity = productService.getProductById(id);
        model.addAttribute("product", productEntity);
        return "product/edit_product";
    }

    @PostMapping("/edit")
    public String update_product(@Valid @ModelAttribute("product") UpdateProductRequest request, BindingResult result){
        if(result.hasErrors()){
            return "product/edit_product";
        }

        productService.updateProduct(request);
        return "redirect:/product";
    }
}
