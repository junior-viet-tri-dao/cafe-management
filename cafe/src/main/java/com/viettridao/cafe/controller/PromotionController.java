package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.request.promotion.CreatePromotionRequest;
import com.viettridao.cafe.dto.request.promotion.UpdatePromotionRequest;
import com.viettridao.cafe.model.PromotionEntity;
import com.viettridao.cafe.service.PromotionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/promotion")
public class PromotionController {
    private final PromotionService promotionService;

//    @GetMapping("")
//    public String list_promotion(Model model){
//        model.addAttribute("promotions",promotionService.getAllPromotion());
//        return "promotion/list_promotion";
//    }

    @GetMapping("")
    public String list_promotion(@RequestParam(required = false) String namePromotion,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "5") int size,
                                 Model model){
        model.addAttribute("promotions",promotionService.getAllPromotionPage(namePromotion,page,size));
        return "promotion/list_promotion";
    }

    @GetMapping("/delete/{id}")
    public String delete_Promomotion(@PathVariable("id") Integer id){
        promotionService.deletePromotion(id);
        return "redirect:/promotion";
    }

    @GetMapping("/insert")
    public String show_form_Promomotion( Model model){
        model.addAttribute("promotion", new CreatePromotionRequest());

        return "promotion/insert_promotion";
    }

    @PostMapping("/insert")
    public String insert_Promomotion(@Valid @ModelAttribute("promotion")  CreatePromotionRequest request, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "promotion/insert_promotion";
        }

        promotionService.createPromotion(request);

        return "redirect:/promotion";
    }

    @GetMapping("/edit/{id}")
    public String show_form_edit_Promomotion(@PathVariable("id") Integer id, Model model){
        PromotionEntity promotionEntity = promotionService.getPromotionById(id);
        model.addAttribute("promotion", promotionEntity);
        return "promotion/edit_promotion";
    }

    @PostMapping("/edit")
    public String update_Promomotion(@Valid @ModelAttribute("promotion") UpdatePromotionRequest request, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            return "promotion/edit_promotion";
        }

        promotionService.updatePromotion(request);

        return "redirect:/promotion";
    }

}
