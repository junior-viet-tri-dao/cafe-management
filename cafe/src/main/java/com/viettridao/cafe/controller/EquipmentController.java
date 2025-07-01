package com.viettridao.cafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.dto.request.equipment.CreateEquipmentRequest;
import com.viettridao.cafe.dto.request.equipment.UpdateEquipmentRequest;
import com.viettridao.cafe.dto.response.equipment.EquipmentResponse;
import com.viettridao.cafe.mapper.EquipmentMapper;
import com.viettridao.cafe.service.EquipmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/equipment")
public class EquipmentController {
    private final EquipmentService equipmentService;
    private final EquipmentMapper equipmentMapper;

    @GetMapping("")
    public String home(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "5") int size,
                       Model model) {
//        List<EquipmentResponse> equipments = equipmentMapper.toEquipmentResponseList(equipmentService.getAllEquipments());
        model.addAttribute("equiments", equipmentService.getAllEquipmentsPage(page, size));
        return "/equipments/equipment";
    }

    @GetMapping("/create")
    public String showFormCreate(Model model) {
        model.addAttribute("equipment", new CreateEquipmentRequest());
        return "/equipments/create_equipment";
    }

    @PostMapping("/create")
    public String createEquipment(@Valid @ModelAttribute("equipment") CreateEquipmentRequest equipment, BindingResult result, RedirectAttributes redirectAttributes) {
        try{
            if (result.hasErrors()) {
                return "/equipments/create_equipment";
            }
            equipmentService.createEquipment(equipment);
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
        	EquipmentResponse response = equipmentMapper.toDto(equipmentService.getEquipmentById(id));
            model.addAttribute("equipment", response);
            return "/equipments/update_equipment";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/equipment";
        }
    }

    @PostMapping("/update")
    public String updateEquipment(@Valid @ModelAttribute UpdateEquipmentRequest request, BindingResult result, RedirectAttributes redirectAttributes) {
        try{
            if (result.hasErrors()) {
                return "/equipments/update_equipment";
            }
            equipmentService.updateEquipment(request);
            redirectAttributes.addFlashAttribute("success", "Chỉnh sửa thiết bị thành công");
            return "redirect:/equipment";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/equipment";
        }
    }

}
