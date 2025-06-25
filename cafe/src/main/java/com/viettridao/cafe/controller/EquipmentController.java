package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.request.equipment.CreateEquipmentRequest;
import com.viettridao.cafe.dto.request.equipment.UpdateEquipmentRequest;
import com.viettridao.cafe.dto.response.equipment.EquipmentResponse;
import com.viettridao.cafe.mapper.EquipmentMapper;
import com.viettridao.cafe.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/equipment")
public class EquipmentController {
    private final EquipmentService equipmentService;
    private final EquipmentMapper equipmentMapper;

    @GetMapping("")
    public String home(Model model) {
        List<EquipmentResponse> equipments = equipmentMapper.toEquipmentResponseList(equipmentService.getAllEquipments());
        model.addAttribute("equiments", equipments);
        return "/equipments/equipment";
    }

    @GetMapping("/create")
    public String showFormCreate() {
        return "/equipments/create_equipment";
    }

    @PostMapping("/create")
    public String createEquipment(@ModelAttribute CreateEquipmentRequest request, RedirectAttributes redirectAttributes) {
        try{
            equipmentService.createEquipment(request);
            redirectAttributes.addFlashAttribute("success", "Thêm thiết bị thành công");
            return "redirect:/equipment";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/equipment/create";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteDevice(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
        try{
            equipmentService.deleteEquipment(id);
            redirectAttributes.addFlashAttribute("success", "Xoá thiết bị thành công");
            return "redirect:/equipment";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/equipment";
        }
    }

    @GetMapping("/update/{id}")
    public String showFormUpdate(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try{
            EquipmentResponse response = equipmentMapper.toEquipmentResponse(equipmentService.getEquipmentById(id));
            model.addAttribute("equipment", response);
            return "/equipments/update_equipment";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/equipment";
        }
    }

    @PostMapping("/update")
    public String showFormUpdate(@ModelAttribute UpdateEquipmentRequest request, RedirectAttributes redirectAttributes) {
        try{
            equipmentService.updateEquipment(request);
            redirectAttributes.addFlashAttribute("success", "Chỉnh sửa thiết bị thành công");
            return "redirect:/equipment";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/equipment";
        }
    }

}
