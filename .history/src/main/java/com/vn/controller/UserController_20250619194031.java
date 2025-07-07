package com.vn.controller;

import com.vn.model.Users;
import com.vn.model.ChucVu;
import com.vn.repository.UserRepository;

import jakarta.validation.Valid;

import com.vn.repository.ChucVuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChucVuRepository chucVuRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @GetMapping("/admin/employee/create")
    public String showCreateEmployeeForm(Model model) {
        model.addAttribute("user", new com.vn.model.Users());
        List<ChucVu> chucVuList = chucVuRepository.findAll();
        model.addAttribute("chucVuList", chucVuList);
        return "admin/employee/create";
    }

    @PostMapping("/admin/employee/create")
    public String createEmployee(@Valid @ModelAttribute("user") Users user, BindingResult result,
                                 @RequestParam("imageFile") MultipartFile imageFile, Model model,
                                 @RequestParam("chucVuId") Integer chucVuId) {
        // Xử lý upload ảnh
        if(user.getPassword().isEmpty()){
            result.rejectValue("password", null, "Passwords not be null");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            result.rejectValue("username", null, "There is already an account registered with that username");
        }



        // if (!imageFile.isEmpty()) {
        //     try {
        //         String uploadDir = "uploads/";
        //         String fileName = imageFile.getOriginalFilename();
        //         Path uploadPath = Paths.get("src/main/resources/static/" + uploadDir);
        //         if (!Files.exists(uploadPath)) {
        //             Files.createDirectories(uploadPath);
        //         }
        //         Path filePath = uploadPath.resolve(fileName);
        //         imageFile.transferTo(filePath);
        //         user.setImage("/" + uploadDir + fileName);
        //     } catch (Exception e) {
        //         e.printStackTrace();
        //     }
        // }
          if (!imageFile.isEmpty()) {
            Path uploadPath = Paths.get("uploads/");
            try {
                Files.createDirectories(uploadPath);
                String encodedFileName = URLEncoder.encode(imageFile.getOriginalFilename(), StandardCharsets.UTF_8.toString());
                Path filePath = uploadPath.resolve(encodedFileName);

                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                user.setImage(filePath.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

         if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
            model.addAttribute("user", user);
            return "admin/employee/create";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/admin/employee/list";
        // Gán chức vụ
        ChucVu chucVu = chucVuRepository.findByMaChucVu(chucVuId);
        user.setChucVu(chucVu);
        // Gán role mặc định
        user.setRole(com.vn.model.Role.Employee);
        // Lưu user
        userRepository.save(user);
        return "redirect:/admin/employee/list";
    }

}
