package com.vn.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
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

import com.vn.model.ChucVu;
import com.vn.model.DonViTinh;
import com.vn.model.HangHoa;
import com.vn.model.Role;
import com.vn.model.Users;
import com.vn.repository.DonViTinhRepository;
import com.vn.repository.HangHoaRepository;

import jakarta.validation.Valid;

@Controller
public class HangHoaController {

    @Autowired
    private HangHoaRepository hangHoaRepository;

    @Autowired
    private DonViTinhRepository donViTinhRepository;

        @GetMapping("/admin/hanghoa/hanghoa-create")
        public String showCreateHangHoaForm(Model model) {
        model.addAttribute("hangHoa", new HangHoa());
        List<DonViTinh> donViTinhList = donViTinhRepository.findAll();
        model.addAttribute("donViTinhList", donViTinhList);
        return "admin/hanghoa/hanghoa-create";
    }

    @PostMapping("/admin/hanghoa/hanghoa-create")
    public String createHangHoa(@Valid @ModelAttribute("hangHoa") HangHoa hangHoa, BindingResult result,
                                 Model model,
                                 @RequestParam("donViTinhId") Integer donViTinhId) {

        if (hangHoaRepository.existsByMaHangHoa(hangHoa.getMaHangHoa())) {
            result.rejectValue("maHangHoa", null, "Mã hàng hóa đã tồn tại");
        }

             if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
            model.addAttribute("hangHoa", hangHoa);
            model.addAttribute("donViTinhList", donViTinhRepository.findAll());
            return "admin/hanghoa/hanghoa-create";
        }
     


        hangHoaRepository.save(hangHoa);
        DonViTinh donViTinh = donViTinhRepository.findByMaDonViTinh(donViTinhId);
        hangHoa.setDonViTinh(donViTinh);
        hangHoaRepository.save(hangHoa);
        return "redirect:/admin/hanghoa/hanghoa-list";
    }

     @GetMapping("/admin/hanghoa/hanghoa-list")
    public String listCustomer( @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "2") int size,
                              @RequestParam(defaultValue = "") String keyword,
                              Model model) {
        Page<HangHoa> posts = hangHoaRepository.searchHangHoa(keyword, PageRequest.of(page, size));

        int totalPages = posts.getTotalPages();

        if (totalPages > 0) {
            int start = Math.max(1, page - 1);
            int end = Math.min(totalPages, page + 3 );

            if (end - start < 4) {
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

        List<Integer> pageNums = new ArrayList();
        for (int i = 1; i <= posts.getTotalPages(); i++) {
            pageNums.add(i);
        }
        model.addAttribute("posts", posts);
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);

        return "admin/hanghoa/hanghoa-list";
    }

}
