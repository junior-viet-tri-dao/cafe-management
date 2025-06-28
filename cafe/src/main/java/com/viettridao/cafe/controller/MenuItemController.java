package com.viettridao.cafe.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.viettridao.cafe.dto.request.menuitem.MenuItemCreateRequest;
import com.viettridao.cafe.dto.request.menuitem.MenuItemUpdateRequest;
import com.viettridao.cafe.dto.response.menuitem.MenuItemResponse;
import com.viettridao.cafe.mapper.MenuItemMapper;
import com.viettridao.cafe.mapper.ProductMapper;
import com.viettridao.cafe.mapper.UnitMapper;
import com.viettridao.cafe.service.MenuItemService;
import com.viettridao.cafe.service.ProductService;
import com.viettridao.cafe.service.UnitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/menu")
public class MenuItemController {
    private final MenuItemService menuItemService;
    private final MenuItemMapper menuItemMapper;
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final UnitService unitService;
    private final UnitMapper unitMapper;

    @GetMapping("")
    public String home(@RequestParam(required = false) String keyword,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "5") int size,
                       Model model) {
        model.addAttribute("menus", menuItemService.getAllMenuItems(keyword, page, size));
        return "/menus/menu";
    }

    @GetMapping("/create")
    public String showFormCreate(Model model) {
        model.addAttribute("products", productMapper.toProductResponse(productService.getAllProducts()));
        model.addAttribute("units", unitMapper.toUnitResponseList(unitService.getAllUnits()));
        model.addAttribute("menu", new MenuItemCreateRequest());
        return "/menus/create_menu";
    }

    @PostMapping("/create")
    public String createMenu(@Valid @ModelAttribute("menu") MenuItemCreateRequest request, BindingResult result,
                                 RedirectAttributes redirectAttributes, Model model) {
        try{
            if (result.hasErrors()) {
                model.addAttribute("products", productMapper.toProductResponse(productService.getAllProducts()));
                model.addAttribute("units", unitMapper.toUnitResponseList(unitService.getAllUnits()));
                return "/menus/create_menu";
            }

            menuItemService.createMenuItem(request);
            redirectAttributes.addFlashAttribute("success", "Thêm thực đơn thành công");
            return "redirect:/menu";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/menu/create";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteMenu(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
        try{
            menuItemService.deleteMenuItem(id);
            redirectAttributes.addFlashAttribute("success", "Xoá thực đơn thành công");
            return "redirect:/menu";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/menu";
        }
    }

    @GetMapping("/update/{id}")
    public String showFormUpdate(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try{
            MenuItemResponse response = menuItemMapper.toResponse(menuItemService.getMenuItemById(id));
            model.addAttribute("products", productMapper.toProductResponse(productService.getAllProducts()));
            model.addAttribute("units", unitMapper.toUnitResponseList(unitService.getAllUnits()));
            model.addAttribute("menu", response);
            return "/menus/update_menu";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/menu";
        }
    }

    @PostMapping("/update")
    public String updateMenu(@Valid @ModelAttribute MenuItemUpdateRequest request, BindingResult result,
                             RedirectAttributes redirectAttributes, Model model) {
        try{
            if (result.hasErrors()) {
                model.addAttribute("units", unitMapper.toUnitResponseList(unitService.getAllUnits()));
                model.addAttribute("products", productMapper.toProductResponse(productService.getAllProducts()));
                return "/menus/update_menu";
            }
            menuItemService.updateMenuItem(request);
            redirectAttributes.addFlashAttribute("success", "Chỉnh sửa thực đơn thành công");
            return "redirect:/menu";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/menu";
        }
    }
}
