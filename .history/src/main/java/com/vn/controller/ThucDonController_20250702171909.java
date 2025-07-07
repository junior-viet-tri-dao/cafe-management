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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vn.model.ThietBi;
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
        Page<ThucDon> posts = thucDonRepository.searchThucDon(keyword, PageRequest.of(page, size));
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


     @GetMapping("/admin/thucdon/thucdon-update/{id}")
    public String showUpdateThucDonForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        ThucDon thucDon = thucDonRepository.findById(id).orElse(null);
        if (thucDon == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy món ăn!");
            return "redirect:/admin/thucdon/thucdon-list";
        }
        model.addAttribute("thucDon", thucDon);
        return "admin/thucdon/thucdon-update";
    }

    @PostMapping("/admin/thucdon/thucdon-update")
    public String updateThucDon(@Valid @ModelAttribute("thucDon") ThucDon thucDon, BindingResult result,
                                Model model, RedirectAttributes redirectAttributes) {
        ThucDon oldThucDon = thucDonRepository.findById(thucDon.getMaThucDon()).orElse(null);
        if (oldThucDon == null) {
            result.rejectValue("maThucDon", null, "Món ăn không tồn tại");
        }
        if (!thucDon.getTenMon().equals(oldThucDon.getTenMon())) {
            if (thucDonRepository.existsByTenMon(thucDon.getTenMon())) {
                result.rejectValue("tenMon", null, "Tên món ăn đã tồn tại");
            }
        }
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
            model.addAttribute("thucDon", thucDon);
            return "admin/thucdon/thucdon-update";
        }

        thucDonRepository.save(thucDon);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật món ăn thành công!");
        return "redirect:/admin/thucdon/thucdon-list";
    }

    
       @PostMapping("/admin/thucdon/delete")
      public String deleteThucDon(@RequestParam("Ids") List<Integer> Ids, Model model) {
        List<ThucDon> thucDons = thucDonRepository.findAllById(Ids);
        for (ThucDon thucDon : thucDons) {
            thucDon.setIsDeleted(true);
            thucDonRepository.save(thucDon);
        }
        model.addAttribute("message", "Đã xóa thành công các món ăn đã chọn.");
        return "redirect:/admin/thucdon/thucdon-list";
    }
}
