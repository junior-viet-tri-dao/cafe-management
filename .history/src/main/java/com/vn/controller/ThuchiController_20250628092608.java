package com.vn.controller;

import com.vn.repository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class ThuchiController {
    @Autowired
    private HoaDonRepository hoaDonRepository;

    @GetMapping("/admin/thuchi")
    public String thuchiPage(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "5") int size,
                             @RequestParam(required = false) Date fromDate,
                             @RequestParam(required = false) Date toDate,
                             Model model) {
        
        Page<Object[]> posts = hoaDonRepository.getDoanhThuChiTieuTheoNgay(
                fromDate != null ? fromDate : null,
                toDate != null ? toDate : null,
                PageRequest.of(page, size)
        );
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
        // if (fromDate != null && !fromDate.isEmpty() && toDate != null && !toDate.isEmpty()) {
        //     from = LocalDate.parse(fromDate);
        //     to = LocalDate.parse(toDate);
        //     List<Object[]> allRows = hoaDonRepository.getDoanhThuChiTieuTheoNgay(Date.valueOf(from), Date.valueOf(to));
        //     int start = Math.min(page * size, allRows.size());
        //     int end = Math.min((page + 1) * size, allRows.size());
        //     if (start < end) {
        //         data = allRows.subList(start, end);
        //     }
        //     posts = new PageImpl<>(data, PageRequest.of(page, size), allRows.size());
        // }
        // int totalPages = posts.getTotalPages();
        // if (totalPages > 0) {
        //     int start = Math.max(1, page - 1);
        //     int end = Math.min(totalPages, page + 3);
        //     if (end - start < 4) {
        //         if (start == 1) {
        //             end = Math.min(totalPages, start + 4);
        //         } else if (end == totalPages) {
        //             start = Math.max(1, end - 4);
        //         }
        //     }
        //     List<Integer> pageNumbers = IntStream.rangeClosed(start, end)
        //             .boxed()
        //             .collect(Collectors.toList());
        //     model.addAttribute("pageNumbers", pageNumbers);
        // }
        // model.addAttribute("posts", posts);
        // model.addAttribute("size", size);
        // model.addAttribute("param", new Object() {
        //     public String getFromDate() { return fromDate; }
        //     public String getToDate() { return toDate; }
        // });
        return "admin/thuchi/thuchi";
    }
} 