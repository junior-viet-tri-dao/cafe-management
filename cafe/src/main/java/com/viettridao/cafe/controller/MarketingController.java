package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.employee.UpdateEmployeeDTO;
import com.viettridao.cafe.dto.marketing.CreateMarketingDTO;
import com.viettridao.cafe.dto.marketing.MarketingDTO;
import com.viettridao.cafe.service.KhuyenMaiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/marketing")
public class MarketingController {
    private final KhuyenMaiService khuyenMaiService;

    @GetMapping("")
    public String home(Model model) {
        model.addAttribute("promotions", khuyenMaiService.getAllMarketings());
        return "/marketing/marketing";
    }

    @GetMapping("/create")
    public String showFromCreateMarketing() {
        return "/marketing/create_marketing";
    }

    @PostMapping("/create")
    public String createMarketing(@ModelAttribute CreateMarketingDTO marketingDTO, RedirectAttributes redirectAttributes) {
        try{
            khuyenMaiService.createMarketing(marketingDTO);
            redirectAttributes.addFlashAttribute("success", "Thêm khuyến mãi thành công");
            return "redirect:/marketing";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/marketing";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteMarketing(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
        try{
            khuyenMaiService.deleteMarketing(id);
            redirectAttributes.addFlashAttribute("success", "Xoá khuyến mãi thành công");
            return "redirect:/marketing";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/marketing";
        }
    }

    @GetMapping("/update/{id}")
    public String updateMarketingForm(@PathVariable("id") Integer id, Model model) {
        MarketingDTO dto = khuyenMaiService.getMarketingDTOById(id);

        if (dto == null) {
            throw new RuntimeException("Không tìm thấy khuyến mãi với ID: " + id);
        }
        model.addAttribute("marketing", dto);
        return "marketing/update_marketing";
    }

    @PostMapping("/update")
    public String updateMarketing(@ModelAttribute MarketingDTO marketingDTO, RedirectAttributes redirectAttributes) {
        try{
            khuyenMaiService.updateMarketing(marketingDTO);
            redirectAttributes.addFlashAttribute("success", "Cập nhật khuyến mãi thành công");
            return "redirect:/marketing";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/marketing";
        }
    }
}
