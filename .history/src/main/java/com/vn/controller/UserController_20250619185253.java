package com.vn.controller;

import com.vn.model.Users;
import com.vn.model.ChucVu;
import com.vn.repository.UserRepository;
import com.vn.repository.ChucVuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChucVuRepository chucVuRepository;

    @GetMapping("/trangcanhan")
    public String getProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userRepository.findByUsername(username);
        ChucVu chucVu = null;
        if (user.getChucVu() != null && user.getChucVu().getMaChucVu() != null) {
            chucVu = chucVuRepository.findByMaChucVu(user.getChucVu().getMaChucVu());
        }
        model.addAttribute("user", user);
        model.addAttribute("chucVu", chucVu);
        return "trangcanhan";
    }

    // @GetMapping("/admin/employee/list")
    // public String listEmployees(Model model,
    //                             @RequestParam(value = "keyword", required = false) String keyword,
    //                             @RequestParam(value = "page", defaultValue = "0") int page,
    //                             @RequestParam(value = "size", defaultValue = "2") int size) {
    //     Pageable pageable = PageRequest.of(page, size);
    //     Page<Users> posts = userRepository.searchEmployee(keyword, pageable);
    //     int totalPages = posts.getTotalPages();
    //     List<Integer> pageNumbers = totalPages > 0 ? java.util.stream.IntStream.range(0, totalPages).boxed().toList() : java.util.Collections.emptyList();
    //     model.addAttribute("posts", posts);
    //     model.addAttribute("pageNumbers", pageNumbers);
    //     model.addAttribute("keyword", keyword);
    //     model.addAttribute("size", size);
    //     return "admin/employee/list";
    // }


       @GetMapping("/admin/employee/list")
      public String listCustomer( @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "2") int size,
                                @RequestParam(defaultValue = "") String keyword,
                                Model model) {
        Page<Users> posts = userRepository.searchEmployee(keyword, PageRequest.of(page, size));

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

        return "admin/employee/list";
    }

}
