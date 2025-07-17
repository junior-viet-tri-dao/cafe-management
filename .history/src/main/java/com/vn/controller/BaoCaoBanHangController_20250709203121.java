package com.vn.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.vn.repository.HoaDonRepository;

@Controller
public class BaoCaoBanHangController {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @GetMapping("/admin/thongke/baocaodoanhthu")
    public String doanhthuPage(@RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "5") int size,
                         @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate fromDate,
                         @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate toDate,
                         Model model) {
    Date from = fromDate != null ? Date.valueOf(fromDate) : null;
    Date to = toDate != null ? Date.valueOf(toDate) : null;

    Page<Object[]> posts = hoaDonRepository.getHoaDonByDateRangeAndStatus(from, to, PageRequest.of(page, size));
        int totalPages = posts.getTotalPages();
            if (totalPages > 0) {
            int start = Math.max(1, page - 1);
            int end = Math.min(totalPages, page + 3);
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
        List<Integer> pageNums = new ArrayList<>();
        for (int i = 1; i <= posts.getTotalPages(); i++) {
            pageNums.add(i);
        }
        model.addAttribute("posts", posts);
        model.addAttribute("size", size);
        return "admin/thongke/baocaohoadon";
    }
    
}
