package com.vn.controller;

import com.vn.model.Users;
import com.vn.model.ChucVu;
import com.vn.model.Role;
import com.vn.repository.UserRepository;

import jakarta.servlet.ServletContext;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import com.vn.auth.CustomUserDetail;
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

    @GetMapping("/chinhsua")
    public String showEditProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
        Users user = userRepository.findById(userDetail.getUsersDB().getMaNhanVien()).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        String fullImagePath =  "/" + user.getImage();
        model.addAttribute("imagePath", fullImagePath);
        return "chinhsua";
    }

    @GetMapping("/admin/employee/update/{id}")
    public String showUpdateEmployeeForm(@PathVariable Integer id, Model model,ServletContext servletContext) {
        Users user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return "redirect:/admin/employee/list";
        }
        model.addAttribute("user", user);
        model.addAttribute("chucVuList", chucVuRepository.findAll());
        // String basePath = servletContext.getContextPath();
        String fullImagePath =  "/" + user.getImage();
        model.addAttribute("imagePath", fullImagePath);
        return "admin/employee/update";
    }

    @PostMapping("/chinhsua")
    public String editProfile(@Valid @ModelAttribute("user") Users user, BindingResult result,
                              @RequestParam(required = false) MultipartFile imageFile, Model model, ServletContext servletContext, RedirectAttributes redirectAttributes) {
       
        Users oldUser = userRepository.findById(user.getMaNhanVien()).orElse(null);
        
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            result.rejectValue("password", null, "Passwords not be null");
        }

        if(!user.getUsername().equals(oldUser.getUsername())){
            if(userRepository.existsByUsername(user.getUsername())){
                result.rejectValue("username", null, "Tên đăng nhập đã tồn tại");
            }
        }

        if(!user.getCMND().equals(oldUser.getCMND())){
            if(userRepository.existsBycMND(user.getCMND())){
                result.rejectValue("cMND", null, "CMND đã tồn tại");
            }
        }

        if(!user.getEmail().equals(oldUser.getEmail())){
            if(userRepository.existsByEmail(user.getEmail())){
                result.rejectValue("email", null, "Email đã tồn tại");
            }
        }

        if(!user.getSoDienThoai().equals(oldUser.getSoDienThoai())){
            if(userRepository.existsBySoDienThoai(user.getSoDienThoai())){
                result.rejectValue("soDienThoai", null, "Số điện thoại đã tồn tại");
            }
        }
        
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            String basePath = servletContext.getContextPath();
            String fullImagePath = user.getImage() != null ? basePath + "/" + user.getImage() : null;
            model.addAttribute("imagePath", fullImagePath);
            return "chinhsua";
        }
        if (imageFile != null && !imageFile.isEmpty()) {
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
        } else if (oldUser != null) {
            user.setImage(oldUser.getImage());
        }
        if (oldUser != null) {
            user.setUsername(oldUser.getUsername());
            user.setRole(oldUser.getRole());
            user.setChucVu(oldUser.getChucVu());
            user.setDeleted(oldUser.isDeleted());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin thành công");
        return "redirect:/trangcanhan";
    }

    @GetMapping("/admin/employee/list")
    public String listCustomer( @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "5") int size,
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
        model.addAttribute("user", new Users());
        List<ChucVu> chucVuList = chucVuRepository.findAll();
        model.addAttribute("chucVuList", chucVuList);
        return "admin/employee/create";
    }

    @PostMapping("/admin/employee/create")
    public String createEmployee(@Valid @ModelAttribute("user") Users user, BindingResult result,
                                 @RequestParam(required = false) MultipartFile imageFile, Model model, RedirectAttributes redirectAttributes,
                                 @RequestParam("chucVuId") Integer chucVuId) {

        if(user.getPassword().isEmpty()){
            result.rejectValue("password", null, "Passwords not be null");
        }

        if(userRepository.existsByUsername(user.getUsername())){
            result.rejectValue("username", null, "Tên đăng nhập đã tồn tại");
        }

        if(userRepository.existsByEmail(user.getEmail())){
            result.rejectValue("email", null, "Email đã tồn tại");
        }
        if(userRepository.existsBySoDienThoai(user.getSoDienThoai())){
            result.rejectValue("soDienThoai", null, "Số điện thoại đã tồn tại");
        }

        if(userRepository.existsBycMND(user.getCMND())){
            result.rejectValue("cMND", null, "CMND đã tồn tại");
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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        ChucVu chucVu = chucVuRepository.findByMaChucVu(chucVuId);
        user.setChucVu(chucVu);
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm nhân viên thành công");
        return "redirect:/admin/employee/list";
    }

    

    @PostMapping("/admin/employee/update")
    public String updateEmployee(@Valid @ModelAttribute("user") Users user, BindingResult result,
                                 @RequestParam(required = false) MultipartFile imageFile, Model model, RedirectAttributes redirectAttributes,
                                 @RequestParam("chucVuId") Integer chucVuId) {

        Users oldUser = userRepository.findById(user.getMaNhanVien()).orElse(null);                            
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            result.rejectValue("password", null, "Passwords not be null");
        }

        if(!user.getUsername().equals(oldUser.getUsername())){
            if(userRepository.existsByUsername(user.getUsername())){
                result.rejectValue("username", null, "Tên đăng nhập đã tồn tại");
            }
        }

        if(!user.getCMND().equals(oldUser.getCMND())){
            if(userRepository.existsBycMND(user.getCMND())){
                result.rejectValue("cMND", null, "CMND đã tồn tại");
            }
        }

        if(!user.getEmail().equals(oldUser.getEmail())){
            if(userRepository.existsByEmail(user.getEmail())){
                result.rejectValue("email", null, "Email đã tồn tại");
            }
        }

        if(!user.getSoDienThoai().equals(oldUser.getSoDienThoai())){
            if(userRepository.existsBySoDienThoai(user.getSoDienThoai())){
                result.rejectValue("soDienThoai", null, "Số điện thoại đã tồn tại");
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("chucVuList", chucVuRepository.findAll());          
            return "admin/employee/update";
        }

        if (imageFile != null && !imageFile.isEmpty()) {
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
        } else {
            if (oldUser != null) {
                user.setImage(oldUser.getImage());
            }
        }
         if (!user.getUsername().equals(oldUser.getUsername())) {
            if (userRepository.existsByUsername(user.getUsername())) {
                result.rejectValue("username", null, "There is already an account registered with that username");
            }
        }
        ChucVu chucVu = chucVuRepository.findByMaChucVu(chucVuId);
        user.setChucVu(chucVu);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật nhân viên thành công");
        return "redirect:/admin/employee/list";
    }

    @PostMapping("/admin/employee/delete")
    public String deleteEmployees(@RequestParam("userIds") List<Integer> userIds, RedirectAttributes redirectAttributes) {
        List<Users> users = userRepository.findAllById(userIds);
        for (Users user : users) {
            user.setDeleted(true);
            userRepository.save(user);
        }
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa thành công các nhân viên đã chọn.");
        return "redirect:/admin/employee/list";
    }

}
