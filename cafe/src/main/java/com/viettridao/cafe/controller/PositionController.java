package com.viettridao.cafe.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.viettridao.cafe.dto.request.position.PositionCreateRequest;
import com.viettridao.cafe.dto.request.position.PositionUpdateRequest;
import com.viettridao.cafe.service.position.IPositionService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/position")
public class PositionController {

    private final IPositionService positionService;

    @GetMapping
    public String listPosition(Model model) {
        model.addAttribute("positions", positionService.getPositionAll());
        return "position/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("position", new PositionCreateRequest());
        return "position/form-create";
    }

    @PostMapping
    public String createPosition(@Valid @ModelAttribute("position") PositionCreateRequest request,
                                 BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "position/form-create";
        }
        positionService.createPosition(request);
        return "redirect:/position";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        PositionUpdateRequest updateRequest = positionService.getUpdateForm(id);
        model.addAttribute("position", updateRequest);
        model.addAttribute("positionId", id);
        return "position/form-edit";
    }

    @PostMapping("/{id}")
    public String updatePosition(@PathVariable Integer id,
                                 @Valid @ModelAttribute("position") PositionUpdateRequest request,
                                 BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("positionId", id);
            return "position/form-edit";
        }
        positionService.updatePosition(id, request);
        return "redirect:/position";
    }

    @PostMapping("/delete/{id}")
    public String deletePosition(@PathVariable Integer id) {
        positionService.deletePosition(id);
        return "redirect:/position";
    }

}
