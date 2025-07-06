package com.viettridao.cafe.controller;

import java.time.LocalDate;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.viettridao.cafe.dto.request.equipment.EquipmentCreateRequest;
import com.viettridao.cafe.dto.request.equipment.EquipmentUpdateRequest;
import com.viettridao.cafe.service.equipment.IEquipmentService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/equipment")
public class EquipmentController {

    private final IEquipmentService equipmentService;

    @GetMapping
    public String listEquipment(Model model) {
        model.addAttribute("equipments", equipmentService.getEquipmentAll());
        return "equipment/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("equipment", new EquipmentCreateRequest());
        return "equipment/form-create";
    }

    @PostMapping
    public String createEquipment(@ModelAttribute("equipment") @Valid EquipmentCreateRequest request,
                                  BindingResult result,
                                  Model model) {
        if (result.hasErrors()) {
            return "equipment/form-create";
        }
        equipmentService.createEquipment(request);
        return "redirect:/equipment";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        EquipmentUpdateRequest updateRequest = equipmentService.getUpdateForm(id);
        model.addAttribute("equipment", updateRequest);
        model.addAttribute("equipmentId", id);
        model.addAttribute("today", LocalDate.now());
        return "equipment/form-edit";
    }

    @PostMapping("/{id}")
    public String updateEquipment(@PathVariable Integer id,
                                  @ModelAttribute("equipment") @Valid EquipmentUpdateRequest request,
                                  BindingResult result,
                                  Model model) {
        if (result.hasErrors()) {
            model.addAttribute("equipmentId", id);
            model.addAttribute("today", LocalDate.now());
            return "equipment/form-edit";
        }
        equipmentService.updateEquipment(id, request);
        return "redirect:/equipment";
    }

    @PostMapping("/delete/{id}")
    public String deleteEquiment(@PathVariable Integer id) {
        equipmentService.deleteEquipment(id);
        return "redirect:/equipment";
    }
}
