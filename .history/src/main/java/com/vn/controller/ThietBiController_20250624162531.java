package com.vn.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.vn.model.Ban;
import com.vn.model.ThietBi;
import com.vn.model.Users;
import com.vn.repository.ThietBiRepository;

import jakarta.validation.Valid;

@Controller
public class ThietBiController {
    @Autowired
    private ThietBiRepository thietBiRepository;

    @GetMapping("/admin/thietbi/thietbi-list")
    public String listTable(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size,
                            @RequestParam(defaultValue = "") String keyword,
                            Model model) {
        Page<ThietBi> posts;
        if (keyword != null && !keyword.isEmpty()) {
            posts = thietBiRepository.findAll(PageRequest.of(page, size)); // TODO: thay bằng searchBan nếu có
        } else {
            posts = thietBiRepository.findAll(PageRequest.of(page, size));
        }
        int totalPages = posts.getTotalPages();
        if (totalPages > 0) {
            int start = Math.max(1, page - 1);
            int end = Math.min(totalPages, page + 3);
            if (end - start< 4) {
                if (start == 1) {
                    end = Math.min(totalPages, start + 4);
                } else if (end == totalPages) {
                    start = Math.max(1, end - 4);
                }
            }
            List<Integer> pageNumbers = IntStream.rangeClosed(start, end)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("posts", posts);
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);
        return "admin/thietbi/thietbi-list";
    }

    @PostMapping("/admin/thietbi/thietbi-create")   
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
