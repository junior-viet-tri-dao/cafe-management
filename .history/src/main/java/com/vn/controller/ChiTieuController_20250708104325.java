package com.vn.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vn.auth.CustomUserDetail;
import com.vn.model.ChiTieu;
import com.vn.repository.ChiTieuRepository;

import jakarta.validation.Valid;

@Controller
public class ChiTieuController {

    @Autowired
    ChiTieuRepository chiTieuRepository;

    @GetMapping("/admin/thuchi/chitieu-list")
    public String listTable(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size,
                            @RequestParam(defaultValue = "") String keyword,
                            Model model) {
        Page<ChiTieu> posts = chiTieuRepository.searchChiTieu(keyword, PageRequest.of(page, size));
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
        return "admin/thuchi/chitieu-list";
    }

    @GetMapping("/admin/thuchi/chitieu-create")
    public String chitieuCreate(Model model) {
        model.addAttribute("chiTieu", new ChiTieu());
        return "admin/thuchi/chitieu-create";
    }

    @PostMapping("/admin/thuchi/chitieu-create")
    public String createTable(@Valid @ModelAttribute("chiTieu") ChiTieu chiTieu, BindingResult result, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
        chiTieu.setNhanVien(userDetail.getUsersDB());

        if(chiTieuRepository.existsByMaChiTieu(chiTieu.getMaChiTieu())) {
            result.rejectValue("maChiTieu", "error.chiTieu", "Mã chi tiêu đã tồn tại");
        }

        if(chiTieuRepository.existsByTenKhoanChi(chiTieu.getTenKhoanChi())) {
            result.rejectValue("tenKhoanChi", "error.chiTieu", "Tên khoản chi đã tồn tại");
        }

        if (chiTieu.getSoTien() == null || chiTieu.getSoTien() <= 0) {
            result.rejectValue("soTien", "error.chiTieu", "Số tiền phải lớn hơn 0");
        }
        if (result.hasErrors()) {
            model.addAttribute("chiTieu", chiTieu);
            return "admin/thuchi/chitieu-create";
        }

        chiTieuRepository.save(chiTieu);
        return "redirect:/admin/thuchi/chitieu-list";
    }



}
