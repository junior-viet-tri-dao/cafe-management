package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.device.UpdateDeviceDTO;
import com.viettridao.cafe.dto.menu.MenuDetailDTO;
import com.viettridao.cafe.repository.DonViRepository;
import com.viettridao.cafe.service.ThucDonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/menu")
public class MenuController {
    private final ThucDonService thucDonService;
    private final DonViRepository donViRepository;

    @GetMapping("")
    public String home(Model model) {
        model.addAttribute("menuItems", thucDonService.getAllMenus());
        return "/menu/menu";
    }

    @GetMapping("/create")
    public String showFormCreateMenu(Model model) {
        model.addAttribute("units", donViRepository.getAllUnits());
        return "/menu/create_menu";
    }

    @PostMapping("/create")
    public String createMenu(@ModelAttribute MenuDetailDTO detailDTO, RedirectAttributes redirectAttributes) {
        try{
            thucDonService.createMenu(detailDTO);
            redirectAttributes.addFlashAttribute("success", "Thêm thực đơn thành công");
            return "redirect:/menu";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/menu";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteMenu(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
        try{

            redirectAttributes.addFlashAttribute("success", "Xoá thực đơn thành công");
            return "redirect:/menu";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/menu";
        }
    }

    @GetMapping("/update/{id}")
    public String updateMenuForm(@PathVariable("id") Integer id, Model model) {
//        ThietBi tb = thietBiService.getDeviceById(id);
//
//        if (tb == null) {
//            throw new RuntimeException("Không tìm thấy thiết bị với ID: " + id);
//        }
//        model.addAttribute("tb", tb);
        return "menu/update_menu";
    }

    @PostMapping("/update")
    public String updateMenu(@ModelAttribute UpdateDeviceDTO deviceDTO, RedirectAttributes redirectAttributes) {
        try{

            redirectAttributes.addFlashAttribute("success", "Cập nhật thực đơn thành công");
            return "redirect:/menu";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/menu";
        }
    }
}
