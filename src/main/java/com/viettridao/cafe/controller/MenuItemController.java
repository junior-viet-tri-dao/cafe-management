package com.viettridao.cafe.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.dto.request.menu_item.MenuItemRequest;
import com.viettridao.cafe.dto.response.menu_item.MenuItemResponse;
import com.viettridao.cafe.dto.response.product.ProductResponse;
import com.viettridao.cafe.mapper.ProductMapper;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.service.MenuItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;

@Controller
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @GetMapping("/menu")
    public String showMenu(@RequestParam(value = "search", required = false) String search, Model model) {
        List<MenuItemResponse> listMenuItems;
        if (search != null && !search.trim().isEmpty()) {
            listMenuItems = menuItemService.searchMenuItemsByName(search.trim());
        } else {
            listMenuItems = menuItemService.getAllMenuItems();
        }
        model.addAttribute("listMenuItems", listMenuItems);
        model.addAttribute("searchKeyword", search);
        return "menus/menu";
    }

    @GetMapping("/menu/create")
    public String showCreateMenu(Model model) {

        List<ProductResponse> listProducts = productRepository.findAll().stream()
                .map(productMapper::convertToDto)
                .toList();
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("createMenuItemRequest", new MenuItemRequest());
        return "menus/menu-create";
    }

    @PostMapping("/menu/create")
    public String createMenu(
            @Valid MenuItemRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // Lấy lại danh sách sản phẩm để render lại form
            List<ProductResponse> listProducts = productRepository.findAll().stream()
                    .map(productMapper::convertToDto)
                    .toList();
            model.addAttribute("listProducts", listProducts);
            model.addAttribute("menuItem", request);
            model.addAttribute("errorMessage", "Vui lòng kiểm tra lại thông tin!");
            return "menus/menu-create";
        }
        try {
            menuItemService.createMenu(request);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm món thành công!");
            return "redirect:/menu";
        } catch (Exception e) {
            e.printStackTrace(); // Thêm dòng này để log lỗi chi tiết ra console
            model.addAttribute("errorMessage", "Có lỗi xảy ra, vui lòng thử lại!");
            List<ProductResponse> listProducts = productRepository.findAll().stream()
                    .map(productMapper::convertToDto)
                    .toList();
            model.addAttribute("listProducts", listProducts);
            model.addAttribute("menuItem", request);
            return "menus/menu-create";
        }
    }

    @GetMapping("/menu/edit/{id}")
    public String showEditMenu(Model model, @PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            List<ProductResponse> listProducts = productRepository.findAll().stream()
                    .map(productMapper::convertToDto)
                    .toList();

            MenuItemResponse menuItemResponse = menuItemService.getMenuItemById(id);

            model.addAttribute("menuItem", menuItemResponse);
            model.addAttribute("listProducts", listProducts);
            return "menus/menu-edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy món hoặc có lỗi xảy ra!");
            return "redirect:/menu";
        }
    }

    @PostMapping("/menu/edit/{id}")
    public String updateMenu(
            @PathVariable Integer id,
            @Valid MenuItemRequest request,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // Lấy lại danh sách sản phẩm để render lại form
            List<ProductResponse> listProducts = productRepository.findAll().stream()
                    .map(productMapper::convertToDto)
                    .toList();
            model.addAttribute("listProducts", listProducts);
            model.addAttribute("menuItem", request);
            model.addAttribute("errorMessage", "Vui lòng kiểm tra lại thông tin!");
            return "menus/menu-edit";
        }
        try {
            menuItemService.updateMenuItem(id, request);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật món thành công!");
            return "redirect:/menu/edit/" + id;
        } catch (Exception e) {
            e.printStackTrace();
            List<ProductResponse> listProducts = productRepository.findAll().stream()
                    .map(productMapper::convertToDto)
                    .toList();
            model.addAttribute("listProducts", listProducts);
            model.addAttribute("menuItem", request);
            model.addAttribute("errorMessage", "Có lỗi xảy ra, vui lòng thử lại!");
            return "menus/menu-edit";
        }
    }

    @GetMapping("/menu/delete/{id}")
    public String deleteMenu(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            menuItemService.deleteMenuItem(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa món thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Xóa món thất bại!");
        }
        return "redirect:/menu";
    }
}
