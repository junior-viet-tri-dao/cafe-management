package com.vn.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        ChucVu chucVu = chucVuRepository.findByMaChucVu(chucVuId);
        user.setChucVu(chucVu);
        user.setRole(Role.Employee);
        userRepository.save(user);
        return "redirect:/admin/employee/list";
    }


    
}
