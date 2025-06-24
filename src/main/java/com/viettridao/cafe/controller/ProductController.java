package com.viettridao.cafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProductController {
    @GetMapping("/product")
    public String showProduct() {
        return "products/product";
    }

    @GetMapping("/product/import")
    public String showImportProduct() {
        return "products/product-import";
    }

    @GetMapping("/product/export")
    public String showExportProduct() {
        return "products/product-export";
    }
}
