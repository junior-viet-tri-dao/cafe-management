package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.commodity.CreateCommodityDTO;
import com.viettridao.cafe.dto.commodity.CreateCommodityExportDTO;
import com.viettridao.cafe.dto.commodity.UpdateCommodityDTO;
import com.viettridao.cafe.repository.DonViRepository;
import com.viettridao.cafe.service.HangHoaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/commodity")
public class CommodityController {
    private final HangHoaService hangHoaService;
    private final DonViRepository donViRepository;
    @GetMapping("")
    public String home(Model model) {
        model.addAttribute("commodities", hangHoaService.getAllCommodities());
        return "/commodity/commodity";
    }

    @GetMapping("/create")
    public String showFromCommodity(Model model) {
        model.addAttribute("commodities", hangHoaService.getAllCommodity());
        model.addAttribute("units", donViRepository.getAllUnits());
        return "/commodity/create_commodity";
    }

    @PostMapping("/create")
    public String createCommodity(@ModelAttribute CreateCommodityDTO createCommodityDTO, RedirectAttributes redirectAttributes) {
        try{
            hangHoaService.createCommodity(createCommodityDTO);
            redirectAttributes.addFlashAttribute("success", "Thêm đơn nhập thành công");
            return "redirect:/commodity";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/commodity";
        }
    }

    @GetMapping("/create/export")
    public String showFromCommodityExport(Model model) {
        model.addAttribute("commodities", hangHoaService.getAllCommodity());
        return "/commodity/create_commodity_export";
    }

    @PostMapping("/create/export")
    public String createCommodityExport(@ModelAttribute CreateCommodityExportDTO exportDTO, RedirectAttributes redirectAttributes) {
        try{
            hangHoaService.createCommodityExport(exportDTO);
            redirectAttributes.addFlashAttribute("success", "Thêm đơn xuất thành công");
            return "redirect:/commodity";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/commodity";
        }
    }

    @GetMapping("/update/{id}")
    public String updateCommodityForm(@PathVariable("id") Integer id, Model model) {
        UpdateCommodityDTO dto = hangHoaService.getUpdateCommodityDTO(id);

        if (dto == null) {
            throw new RuntimeException("Không tìm thấy đơn nhập với ID: " + id);
        }
        model.addAttribute("commodity", dto);
        model.addAttribute("commodities", hangHoaService.getAllCommodity());
        return "commodity/update_commodity";
    }

    @PostMapping("/update")
    public String updateCommodity(@ModelAttribute UpdateCommodityDTO commodityDTO, RedirectAttributes redirectAttributes) {
        try{
            hangHoaService.updateCommodity(commodityDTO);
            redirectAttributes.addFlashAttribute("success", "Cập nhật đơn nhập thành công");
            return "redirect:/commodity";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/commodity";
        }
    }
}
