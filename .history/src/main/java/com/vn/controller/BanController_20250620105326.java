package com.vn.controller;

import com.vn.model.Ban;
import com.vn.model.Users;
import com.vn.repository.BanRepository;
import com.vn.model.TinhTrangBan;
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

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class BanController {
    @Autowired
    private BanRepository banRepository;

    @GetMapping("/admin/sales/table-create")
    public String showCreateTableForm(Model model) {
        model.addAttribute("table", new Ban());
        model.addAttribute("tinhTrangList", TinhTrangBan.values());
        return "admin/sales/table-create";
    }

    @PostMapping("/admin/sales/table-create")
    public String createTable(@Valid @ModelAttribute("table") Ban table, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("table", table);
            model.addAttribute("tinhTrangList", TinhTrangBan.values());
            return "admin/sales/table-create";
        }
        banRepository.save(table);
        return "redirect:/admin/sales/table-list";
    }

    @GetMapping("/admin/sales/table-list")
    public String listTable(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size,
                            @RequestParam(defaultValue = "") String keyword,
                            Model model) {
        Page<Ban> posts;
        if (keyword != null && !keyword.isEmpty()) {
            posts = banRepository.findAll(PageRequest.of(page, size)); // TODO: thay bằng searchBan nếu có
        } else {
            posts = banRepository.findAll(PageRequest.of(page, size));
        }
        int totalPages = posts.getTotalPages();
        if (totalPages > 0) {
            int start = Math.max(1, page - 1);
            int end = Math.min(totalPages, page + 3);
            if (end - sttabl< 4) {
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
        return "admin/sales/table-list";
    }
}