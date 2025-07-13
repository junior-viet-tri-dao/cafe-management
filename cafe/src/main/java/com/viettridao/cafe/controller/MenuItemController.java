package com.viettridao.cafe.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.dto.request.menu.MenuItemRequest;
import com.viettridao.cafe.dto.response.menu.MenuItemResponse;
import com.viettridao.cafe.service.MenuItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuItemController {

	private final MenuItemService menuItemService;

	@GetMapping
	public String listMenuItems(@RequestParam(defaultValue = "") String keyword,
	                            @RequestParam(defaultValue = "0") int page,
	                            @RequestParam(defaultValue = "10") int size,
	                            Model model) {
		Page<MenuItemResponse> items = menuItemService.getAll(keyword, page, size);
		model.addAttribute("items", items);
		model.addAttribute("keyword", keyword);
		return "menu/list";
	}

	@GetMapping("/add")
	public String showAddForm(Model model) {
		model.addAttribute("menuItem", new MenuItemRequest());
		return "menu/add";
	}

	@PostMapping("/add")
	public String addMenuItem(@ModelAttribute @Valid MenuItemRequest menuItem,
	                          BindingResult result,
	                          Model model,
	                          RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "menu/add";
		}

		try {
			menuItemService.add(menuItem);
			redirectAttributes.addFlashAttribute("success", "Thêm món mới thành công!");
			return "redirect:/menu";
		} catch (RuntimeException ex) {
			model.addAttribute("error", ex.getMessage());
			return "menu/add";
		}
	}

	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Integer id, Model model) {
		MenuItemResponse item = menuItemService.getById(id);
		model.addAttribute("menuItem", item);
		return "menu/edit";
	}

	@PostMapping("/edit/{id}")
	public String updateMenuItem(@PathVariable Integer id,
	                             @ModelAttribute @Valid MenuItemRequest menuItem,
	                             BindingResult result,
	                             Model model,
	                             RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "menu/edit";
		}

		try {
			menuItemService.update(id, menuItem);
			redirectAttributes.addFlashAttribute("success", "Cập nhật món thành công!");
			return "redirect:/menu";
		} catch (RuntimeException ex) {
			model.addAttribute("error", ex.getMessage());
			return "menu/edit";
		}
	}

	@PostMapping("/delete/{id}")
	public String deleteMenuItem(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		menuItemService.delete(id);
		redirectAttributes.addFlashAttribute("success", "Xóa món thành công!");
		return "redirect:/menu";
	}

	@GetMapping("/detail/{id}")
	public String viewDetail(@PathVariable Integer id, Model model) {
		MenuItemResponse item = menuItemService.getById(id);
		model.addAttribute("item", item);
		return "menu/detail";
	}
}
