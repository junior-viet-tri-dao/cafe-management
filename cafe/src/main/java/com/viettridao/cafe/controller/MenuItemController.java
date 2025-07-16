package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.request.menu.CreateMenuItemRequest;
import com.viettridao.cafe.dto.request.menu.UpdateMenuItemRequest;
import com.viettridao.cafe.model.MenuDetailEntity;
import com.viettridao.cafe.model.MenuItemEntity;
import com.viettridao.cafe.service.MenuItemService;
import com.viettridao.cafe.service.ProductService;
import com.viettridao.cafe.service.UnitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/menu")
public class MenuItemController {
    private final MenuItemService menuItemService;
    private final ProductService productService;
    private final UnitService unitService;


    @GetMapping("")
    public String showListMenu(@RequestParam(required = false) String keyword,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size,
                               Model model){
        model.addAttribute("MenuItems", menuItemService.getAllMenuItemPage(keyword, page, size));
        return "/menu/list_menu";
    }

    @GetMapping("/insert")
    public String showFormInsertMenu(Model model){
        model.addAttribute("MenuItem", new CreateMenuItemRequest());
        model.addAttribute("products",productService.getAllProduct());
        model.addAttribute("Units",unitService.getAllUnit());
        return "/menu/insert_menu";
    }

    @PostMapping("/insert")
    public  String insertMenu(@Valid @ModelAttribute("MenuItem") CreateMenuItemRequest createMenuRequest, BindingResult result){
        if(result.hasErrors()){
            return "redirect:/menu";
        }


        menuItemService.createMenu(createMenuRequest);

        return "redirect:/menu";
    }

//    @GetMapping("/edit/{id}")
//    public String showFormEditMenu(@PathVariable("id") Integer id, Model model){
//        MenuItemEntity menuItemEntity = menuItemService.getMenuItemByID(id);
//        model.addAttribute("units", unitService.getAllUnit() );
//        model.addAttribute("products", productService.getAllProduct());
//        model.addAttribute("MenuItem", menuItemEntity);
//
//
//        return "/menu/edit";
//    }
//
//    @PostMapping("edit")
//    public String editMenuItem(@Valid @ModelAttribute("MenuItem") UpdateMenuItemRequest request, BindingResult result){
//        if(result.hasErrors()){
//            return "/menu";
//        }
//
//        menuItemService.updateMenuItem(request);
//
//        return "/menu";
//    }



    @GetMapping("/delete/{id}")
    public String deleteMenuItem(@PathVariable("id") Integer id, Model model){
        menuItemService.deleteMenuItemById(id);

        return "redirect:/menu";
    }
}
