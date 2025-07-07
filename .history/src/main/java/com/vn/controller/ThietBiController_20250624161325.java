package com.vn.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.vn.model.Users;

import jakarta.validation.Valid;

@Controller
public class ThietBiController {

    @PostMapping("/admin/employee/create")
    public String createEmployee(@Valid @ModelAttribute("user") Users user, BindingResult result,
                                 @RequestParam(required = false) MultipartFile imageFile, Model model,
                                 @RequestParam("chucVuId") Integer chucVuId) {
        if(user.getPassword().isEmpty()){
            result.rejectValue("password", null, "Passwords not be null");
        }

             if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
            model.addAttribute("user", user);
            model.addAttribute("chucVuList", chucVuRepository.findAll());
            return "admin/employee/create";
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            result.rejectValue("username", null, "There is already an account registered with that username");
        }

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
}
