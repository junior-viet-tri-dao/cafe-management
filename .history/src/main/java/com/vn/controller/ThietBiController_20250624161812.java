package com.vn.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.vn.model.ThietBi;
import com.vn.model.Users;
import com.vn.repository.ThietBiRepository;

import jakarta.validation.Valid;

@Controller
public class ThietBiController {
    @Autowired
    private ThietBiRepository thietBiRepository;

    

    @PostMapping("/admin/thietbi/create")
    public String createThietBi(@Valid @ModelAttribute("thietBi") ThietBi thietBi, BindingResult result,
                            Model model) {
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
            model.addAttribute("thietBi", thietBi);
            return "admin/thietbi/create";
        }
        thietBiRepository.save(thietBi);
        return "redirect:/admin/thietbi/thietbi-list";
    }
}
