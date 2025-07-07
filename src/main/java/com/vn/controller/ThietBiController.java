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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.vn.model.ThietBi;
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
        Page<ThietBi> posts = thietBiRepository.searchThietBi(keyword, PageRequest.of(page, size));
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
    public String createTable(@Valid @ModelAttribute("thietBi") ThietBi thietBi, BindingResult result, Model model,RedirectAttributes redirectAttributes) {
        
        if(thietBiRepository.existsByMaThietBi(thietBi.getMaThietBi())) {
            result.rejectValue("maThietBi", "error.thietBi", "Mã thiết bị đã tồn tại");
        }

        if(thietBiRepository.existsByTenThietBi(thietBi.getTenThietBi())) {
            result.rejectValue("tenThietBi", "error.thietBi", "Tên thiết bị đã tồn tại");
        }

        if (result.hasErrors()) {
            model.addAttribute("thietBi", thietBi);
            return "admin/thietbi/thietbi-create";
        }
        thietBiRepository.save(thietBi);  
        redirectAttributes.addFlashAttribute("successMessage", "Thêm thiết bị thành công");
        return "redirect:/admin/thietbi/thietbi-list";
    }


    @GetMapping("/admin/thietbi/thietbi-create")
    public String showCreateThietBiForm(Model model) {
        model.addAttribute("thietBi", new ThietBi());
        return "admin/thietbi/thietbi-create";
    }

    @GetMapping("/admin/thietbi/update/{id}")
    public String showUpdateThietBiForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        ThietBi thietBi = thietBiRepository.findById(id).orElse(null);
        if (thietBi == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy thiết bị!");
            return "redirect:/admin/thietbi/thietbi-list";
        }
        model.addAttribute("thietBi", thietBi);
        return "admin/thietbi/thietbi-update";
    }

    @PostMapping("/admin/thietbi/update")
    public String updateThietBi(@Valid @ModelAttribute("thietBi") ThietBi thietBi, BindingResult result, RedirectAttributes redirectAttributes,
                                Model model) {

        ThietBi oldThietBi = thietBiRepository.findById(thietBi.getMaThietBi()).orElse(null);

        if (oldThietBi == null) {
            result.rejectValue("maThietBi", null, "Thiết bị không tồn tại");
        }

        if (!thietBi.getTenThietBi().equals(oldThietBi.getTenThietBi())) {
            if (thietBiRepository.existsByTenThietBi(thietBi.getTenThietBi())) {
                result.rejectValue("tenThietBi", null, "Tên thiết bị đã tồn tại");
            }
        }
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
            model.addAttribute("thietBi", thietBi);
            return "admin/thietbi/thietbi-update";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thiết bị thành công");
        thietBiRepository.save(thietBi);
        return "redirect:/admin/thietbi/thietbi-list";
    }

       @PostMapping("/admin/thietbi/delete")
      public String deleteThietBi(@RequestParam("thietBiIds") List<Integer> thietBiIds, RedirectAttributes redirectAttributes) {
        
        List<ThietBi> thietBis = thietBiRepository.findAllById(thietBiIds);
        
        for (ThietBi thietBi : thietBis) {
            thietBi.setDeleted(true);
            thietBiRepository.save(thietBi);
        }
        
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa thành công các thiết bị đã chọn.");
        return "redirect:/admin/thietbi/thietbi-list";
    }


}
