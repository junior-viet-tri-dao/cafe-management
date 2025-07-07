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
import com.vn.model.KhuyenMai;
import com.vn.repository.KhuyenMaiRepository;

import jakarta.validation.Valid;

@Controller
public class KhuyenMaiController {
    
    @Autowired
    private KhuyenMaiRepository khuyenMaiRepository;

    @GetMapping("/admin/khuyenmai/khuyenmai-create")
    public String showCreateKhuyenMaiForm(Model model) {
        model.addAttribute("khuyenMai", new KhuyenMai());
        return "admin/khuyenmai/khuyenmai-create";
    }
    
    
    @GetMapping("/admin/khuyenmai/khuyenmai-list")
    public String listTable(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size,
                            @RequestParam(defaultValue = "") String keyword,
                            Model model) {
        Page<KhuyenMai> posts = khuyenMaiRepository.searchKhuyenMai(keyword, PageRequest.of(page, size));
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
        return "admin/khuyenmai/khuyenmai-list";
    }

    @PostMapping("/admin/khuyenmai/khuyenmai-create")
    public String createTable(@Valid @ModelAttribute("khuyenMai") KhuyenMai khuyenMai, BindingResult result, Model model) {

        if (khuyenMaiRepository.existsByTenKhuyenMai(khuyenMai.getTenKhuyenMai())) {
                result.rejectValue("tenKhuyenMai", null, "Tên khuyến mãi đã tồn tại");
            }
        if (result.hasErrors()) {
            model.addAttribute("khuyenMai", khuyenMai);
            return "admin/khuyenmai/khuyenmai-create";
        }
        khuyenMaiRepository.save(khuyenMai);
        return "redirect:/admin/khuyenmai/khuyenmai-list";
    }

    @GetMapping("/admin/khuyenmai/khuyenmai-update/{id}")
    public String showUpdateKhuyenMaiForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        KhuyenMai khuyenMai = khuyenMaiRepository.findById(id).orElse(null);
        if (khuyenMai == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy khuyến mãi!");
            return "redirect:/admin/khuyenmai/khuyenmai-list";
        }
        model.addAttribute("khuyenMai", khuyenMai);
        return "admin/khuyenmai/khuyenmai-update";
    }

    @PostMapping("/admin/khuyenmai/khuyenmai-update")
    public String updateKhuyenMai(@Valid @ModelAttribute("khuyenMai") KhuyenMai khuyenMai, BindingResult result,
                                Model model, RedirectAttributes redirectAttributes) {
        KhuyenMai oldKhuyenMai = khuyenMaiRepository.findById(khuyenMai.getMaKhuyenMai()).orElse(null);
        if (oldKhuyenMai == null) {
            result.rejectValue("maKhuyenMai", null, "Khuyến mãi không tồn tại");
        }
        if (!khuyenMai.getTenKhuyenMai().equals(oldKhuyenMai.getTenKhuyenMai())) {
            if (khuyenMaiRepository.existsByTenKhuyenMai(khuyenMai.getTenKhuyenMai())) {
                result.rejectValue("tenKhuyenMai", null, "Tên khuyến mãi đã tồn tại");
            }
        }
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
            model.addAttribute("khuyenMai", khuyenMai);
            return "admin/khuyenmai/khuyenmai-update";
        }

        khuyenMaiRepository.save(khuyenMai);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật khuyến mãi thành công!");
        return "redirect:/admin/khuyenmai/khuyenmai-list";
    }

    @PostMapping("/admin/khuyenmai/delete")
    public String deleteKhuyenMai(@RequestParam("Ids") List<Integer> Ids, Model model) {

        List<KhuyenMai> khuyenMais = khuyenMaiRepository.findAllById(Ids);
        for (KhuyenMai khuyenMai : khuyenMais) {
            khuyenMai.setDeleted(true);
            khuyenMaiRepository.save(khuyenMai);
        }

        model.addAttribute("message", "Đã xóa thành công các khuyến mãi đã chọn.");
        return "redirect:/admin/khuyenmai/khuyenmai-list";
    }

}
