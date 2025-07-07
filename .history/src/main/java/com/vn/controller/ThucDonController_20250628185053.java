package com.vn.controller;

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


import com.vn.model.ThucDon;
import com.vn.repository.ThucDonRepository;

import jakarta.validation.Valid;

@Controller
public class ThucDonController {

    @Autowired
    private ThucDonRepository thucDonRepository;
    
        @GetMapping("/admin/thucdon/thucdon-list")
    public String listTable(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size,
                            @RequestParam(defaultValue = "") String keyword,
                            Model model) {
        Page<ThucDon> posts;
        if (keyword != null && !keyword.isEmpty()) {
            posts = thucDonRepository.findAll(PageRequest.of(page, size)); // TODO: thay bằng searchBan nếu có
        } else {
            posts = thucDonRepository.findAll(PageRequest.of(page, size));
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
        return "admin/thucdon/thucdon-list";
    }

    @GetMapping("/admin/thucdon/thucdon-create")
    public String thucdonCreate(Model model) {
        model.addAttribute("thucDon", new ThucDon());
        return "admin/thucdon/thucdon-create";
    }

    @PostMapping("/admin/thucdon/thucdon-create")
    public String createTable(@Valid @ModelAttribute("thucDon") ThucDon thucDon, BindingResult result, Model model) {


        if(thucDonRepository.existsByTenMon(thucDon.getTenMon())) {
            result.rejectValue("tenMon", "error.thucDon", "Tên món đã tồn tại");
        }

        if (result.hasErrors()) {
            model.addAttribute("thucDon", thucDon);
            return "admin/thucdon/thucdon-create";
        }

        thucDon.setIsDeleted(false);

        thucDonRepository.save(thucDon);
        return "redirect:/admin/thucdon/thucdon-list";
    }

}
