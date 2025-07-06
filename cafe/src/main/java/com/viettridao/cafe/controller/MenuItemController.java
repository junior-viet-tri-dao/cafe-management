package com.viettridao.cafe.controller;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.viettridao.cafe.dto.request.menuitem.MenuItemCreateRequest;
import com.viettridao.cafe.dto.request.menuitem.MenuItemUpdateRequest;
import com.viettridao.cafe.service.menuItem.IMenuItemService;
import com.viettridao.cafe.service.product.IProductService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/menuItem")
public class MenuItemController {
    
    private final IMenuItemService menuItemService;
    private final IProductService productService;

    @GetMapping
    public String listMenuItem(Model model) {
        model.addAttribute("menuItems" , menuItemService.getMenuItemAll());
        return "menuItem/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model){
        model.addAttribute("menuItem", new MenuItemCreateRequest());
        model.addAttribute("products", productService.getProductAll());
        return "menuItem/form-create";
    }

    @PostMapping
    public String createMenuItem(@Valid @ModelAttribute("menuItem") MenuItemCreateRequest request,
                                 BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("products", productService.getProductAll());
            return "menuItem/form-create";
        }
        menuItemService.createMenuItem(request);
        return "redirect:/menuItem";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm (@PathVariable Integer id, Model model) {
        MenuItemUpdateRequest updateRequest = menuItemService.getUpdateForm(id);
        model.addAttribute("menuItem", updateRequest);
        model.addAttribute("menuItemId", id);
        model.addAttribute("products", productService.getProductAll());
        return "menuItem/form-edit";
    }

    @PostMapping("/{id}")
    public String updateMenuItem(@PathVariable Integer id,
                                 @Valid @ModelAttribute("menuItem") MenuItemUpdateRequest request,
                                 BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("products", productService.getProductAll());
            model.addAttribute("menuItemId", id);
            return "menuItem/form-edit";
        }
        menuItemService.updateMenuItem(id, request);
        return "redirect:/menuItem";
    }

    @PostMapping("/delete/{id}")
    public String deletemenuItem(@PathVariable Integer id) {
        menuItemService.deleteMenuItem(id);
        return "redirect:/menuItem";
    }
}
