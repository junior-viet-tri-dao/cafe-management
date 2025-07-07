// package com.vn.controller;


// import com.vn.model.Role;
// import com.vn.model.Users;
// import com.vn.repository.UserRepository;
// import jakarta.servlet.ServletContext;
// import jakarta.validation.Valid;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.validation.BindingResult;
// import org.springframework.web.bind.annotation.*;
// import org.springframework.web.multipart.MultipartFile;

// import java.io.IOException;
// import java.net.URLEncoder;
// import java.nio.charset.StandardCharsets;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.nio.file.StandardCopyOption;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.stream.Collectors;
// import java.util.stream.IntStream;

// @Controller
// public class UsersController {

//     @Autowired
//     UserRepository userRepository;


//     @Autowired
//     private ServletContext servletContext;

//     @Autowired
//     private PasswordEncoder passwordEncoder;

//     // Controller Customer
//     @GetMapping("/admin/customer/create")
//     public String showCreateCustomer(Model model) {
//         model.addAttribute("user", new Users());
//         return "admin/customer/create";
//     }

//     @GetMapping("/admin/customer/update")
//     public String showUpdateCustomer(Model model) {
//         model.addAttribute("user", new Users());
//         return "admin/customer/update";
//     }

//     @PostMapping("/admin/customer/delete")
//     public String deleteCustomers(@RequestParam("userIds") List<Integer> userIds) {
//         userRepository.deleteAllById(userIds);
//         return "redirect:/admin/customer/list";
//     }




//     @GetMapping("/admin/customer/update/{id}")
//     public String showUpdateCustomerForm(@PathVariable Integer id, Model model) {
//         Users user = userRepository.findByUserIdAndRole(id, Role.Customer);
//         model.addAttribute("user", user);
//         String basePath = servletContext.getContextPath();
//         String fullImagePath = basePath + "/" + user.getImage();
//         model.addAttribute("imagePath", fullImagePath);

//         return "admin/customer/update";
//     }


//     @PostMapping("/admin/customer/create")
//     public String createCustomer(
//             @Valid @ModelAttribute("user") Users user,
//             BindingResult result,
//             Model model,
//             @RequestParam(required = false) MultipartFile imageFile) {

//         if (!user.getPassword().equals(user.getRePassword())) {
//             result.rejectValue("rePassword", null, "Passwords do not match");
//         }

//         if(user.getPassword().isEmpty()){
//             result.rejectValue("password", null, "Passwords not be null");
//         }

//         if(user.getRePassword().isEmpty()){
//             result.rejectValue("rePassword", null, "Re password not be null");
//         }

//         if (userRepository.existsByUsername(user.getUsername())) {
//             result.rejectValue("username", null, "There is already an account registered with that username");
//         }

//         if (userRepository.existsByEmail(user.getEmail())) {
//             result.rejectValue("email", null, "There is already an account registered with that email");
//         }

//         if (userRepository.existsByPhone(user.getPhone())) {
//             result.rejectValue("phone", null, "There is already an account registered with that phone number");
//         }


//         if (userRepository.existsByIdentityCard(user.getIdentityCard())) {
//             result.rejectValue("identityCard", null, "There is already an account registered with that identity card");
//         }


//         if (!imageFile.isEmpty()) {
//             Path uploadPath = Paths.get("uploads/");
//             try {
//                 Files.createDirectories(uploadPath);
//                 String encodedFileName = URLEncoder.encode(imageFile.getOriginalFilename(), StandardCharsets.UTF_8.toString());
//                 Path filePath = uploadPath.resolve(encodedFileName);

//                 Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//                 user.setImage(filePath.toString());
//             } catch (IOException e) {
//                 throw new RuntimeException(e);
//             }
//         }

//         if (result.hasErrors()) {
//             result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
//             model.addAttribute("user", user);
//             return "admin/customer/create";
//         }

//         user.setPassword(passwordEncoder.encode(user.getPassword()));
//         userRepository.save(user);
//         return "redirect:/admin/customer/list";
//     }


//     @PostMapping("/admin/customer/update")
//     public String updateCustomer(
//             @Valid @ModelAttribute("user") Users user,
//             BindingResult result,
//             Model model,
//             @RequestParam(required = false) MultipartFile imageFile) {

//         Users oldUser = userRepository.findById(user.getUserId()).orElseThrow(() -> new RuntimeException("not found"));

//         if(!user.getPassword().isEmpty()){
//             if (!user.getPassword().equals(user.getRePassword())) {
//                 result.rejectValue("rePassword", null, "Passwords do not match");
//             }
//             else{
//                 user.setPassword(passwordEncoder.encode(user.getPassword()));
//             }
//         }
//         else{
//             user.setPassword(oldUser.getPassword());
//         }

//         if (!imageFile.isEmpty()) {
//             Path uploadPath = Paths.get("uploads/");
//             try {
//                 Files.createDirectories(uploadPath);
//                 String encodedFileName = URLEncoder.encode(imageFile.getOriginalFilename(), StandardCharsets.UTF_8.toString());
//                 Path filePath = uploadPath.resolve(encodedFileName);

//                 Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//                 user.setImage(filePath.toString());
//             } catch (IOException e) {
//                 throw new RuntimeException(e);
//             }
//         }
//         else{
//             user.setImage(oldUser.getImage());
//         }


//         if (!user.getEmail().equals(oldUser.getEmail())) {
//             if (userRepository.existsByEmail(user.getEmail())) {
//                 result.rejectValue("email", null, "There is already an account registered with that email");
//             }
//         }

//         if (!user.getUsername().equals(oldUser.getUsername())) {
//             if (userRepository.existsByUsername(user.getUsername())) {
//                 result.rejectValue("username", null, "There is already an account registered with that username");
//             }
//         }

//         if (!user.getPhone().equals(oldUser.getPhone())) {
//             if (userRepository.existsByPhone(user.getPhone())) {
//                 result.rejectValue("phone", null, "There is already an account registered with that phone number");
//             }
//         }

//         if (!user.getIdentityCard().equals(oldUser.getIdentityCard())) {
//             if (userRepository.existsByIdentityCard(user.getIdentityCard())) {
//                 result.rejectValue("identityCard", null, "There is already an account registered with that identity card");
//             }
//         }

//         if (result.hasErrors()) {
//             result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
//             model.addAttribute("user", user);
//             return "admin/customer/update";
//         }

//         userRepository.save(user);
//         return "redirect:/admin/customer/list";
//     }

//     @GetMapping("/admin/customer/list")
//     public String listCustomer( @RequestParam(defaultValue = "0") int page,
//                                 @RequestParam(defaultValue = "5") int size,
//                                 @RequestParam(defaultValue = "") String keyword,
//                                 Model model) {
//         Page<Users> posts = userRepository.searchCustomer(keyword, PageRequest.of(page, size));

//         int totalPages = posts.getTotalPages();

//         if (totalPages > 0) {
//             int start = Math.max(1, page - 1);
//             int end = Math.min(totalPages, page + 3 );

//             if (end - start < 4) {
//                 if (start == 1) {
//                     end = Math.min(totalPages, start + 4);
//                 } else if (end == totalPages) {
//                     start = Math.max(1, end - 4);
//                 }
//             }

//             List<Integer> pageNumbers = IntStream.rangeClosed(start, end)
//                     .boxed()
//                     .collect(Collectors.toList());
//             model.addAttribute("pageNumbers", pageNumbers);
//         }

//         List<Integer> pageNums = new ArrayList<>();
//         for (int i = 1; i <= posts.getTotalPages(); i++) {
//             pageNums.add(i);
//         }
//         model.addAttribute("posts", posts);
//         model.addAttribute("size", size);
//         model.addAttribute("keyword", keyword);

//         return "admin/customer/list";
//     }

// //  Controller Employee

//     @GetMapping("/admin/employee/create")
//     public String showCreateEmployee(Model model) {
//         model.addAttribute("user", new Users());
//         return "admin/employee/create";
//     }

//     @GetMapping("/admin/employee/update")
//     public String showUpdateEmployee(Model model) {
//         model.addAttribute("user", new Users());
//         return "admin/employee/update";
//     }

//     @PostMapping("/admin/employee/delete")
//     public String deleteEmployees(@RequestParam("userIds") List<Integer> userIds) {

//         userRepository.deleteAllById(userIds);
//         return "redirect:/admin/employee/list";
//     }

//     @GetMapping("/admin/employee/update/{id}")
//     public String showUpdateEmployeeForm(@PathVariable Integer id, Model model) {
//         Users user = userRepository.findByUserIdAndRole(id, Role.Employee);
//         model.addAttribute("user", user);

//         String basePath = servletContext.getContextPath();
//         String fullImagePath = basePath + "/" + user.getImage();
//         model.addAttribute("imagePath", fullImagePath);
//         return "admin/employee/update";
//     }


//     @PostMapping("/admin/employee/create")
//     public String createEmployee(
//             @Valid @ModelAttribute("user") Users user,
//             BindingResult result,
//             Model model,
//             @RequestParam(required = false) MultipartFile imageFile) {

//         if (!user.getPassword().equals(user.getRePassword())) {
//             result.rejectValue("rePassword", null, "Passwords do not match");
//         }

//         if(user.getPassword().isEmpty()){
//             result.rejectValue("password", null, "Passwords not be null");
//         }

//         if(user.getRePassword().isEmpty()){
//             result.rejectValue("rePassword", null, "Re password not be null");
//         }

//         if (userRepository.existsByUsername(user.getUsername())) {
//             result.rejectValue("username", null, "There is already an account registered with that username");
//         }


//         if (userRepository.existsByEmail(user.getEmail())) {
//             result.rejectValue("email", null, "There is already an account registered with that email");
//         }

//         if (userRepository.existsByPhone(user.getPhone())) {
//             result.rejectValue("phone", null, "There is already an account registered with that phone number");
//         }

//         if (userRepository.existsByIdentityCard(user.getIdentityCard())) {
//             result.rejectValue("identityCard", null, "There is already an account registered with that identity card");
//         }


//         if (!imageFile.isEmpty()) {
//             Path uploadPath = Paths.get("uploads/");
//             try {
//                 Files.createDirectories(uploadPath);
//                 String encodedFileName = URLEncoder.encode(imageFile.getOriginalFilename(), StandardCharsets.UTF_8.toString());
//                 Path filePath = uploadPath.resolve(encodedFileName);

//                 Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//                 user.setImage(filePath.toString());
//             } catch (IOException e) {
//                 throw new RuntimeException(e);
//             }
//         }

//         if (result.hasErrors()) {
//             result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
//             model.addAttribute("user", user);
//             return "admin/employee/create";
//         }

//         user.setPassword(passwordEncoder.encode(user.getPassword()));
//         userRepository.save(user);
//         return "redirect:/admin/employee/list";
//     }


//     @PostMapping("/admin/employee/update")
//     public String updateEmployee(
//             @Valid @ModelAttribute("user") Users user,
//             BindingResult result,
//             Model model,
//             @RequestParam(required = false) MultipartFile imageFile) {

//         Users oldUser = userRepository.findById(user.getUserId()).orElseThrow(() -> new RuntimeException("not found"));

//         if(!user.getPassword().isEmpty()){
//             if (!user.getPassword().equals(user.getRePassword())) {
//                 result.rejectValue("rePassword", null, "Passwords do not match");
//             }
//             else{
//                 user.setPassword(passwordEncoder.encode(user.getPassword()));
//             }
//         }
//         else{
//             user.setPassword(oldUser.getPassword());
//         }

//         if (!imageFile.isEmpty()) {
//             Path uploadPath = Paths.get("uploads/");
//             try {
//                 Files.createDirectories(uploadPath);
//                 String encodedFileName = URLEncoder.encode(imageFile.getOriginalFilename(), StandardCharsets.UTF_8.toString());
//                 Path filePath = uploadPath.resolve(encodedFileName);

//                 Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//                 user.setImage(filePath.toString());
//             } catch (IOException e) {
//                 throw new RuntimeException(e);
//             }
//         }
//         else{
//             user.setImage(oldUser.getImage());
//         }


//         if (!user.getEmail().equals(oldUser.getEmail())) {
//             if (userRepository.existsByEmail(user.getEmail())) {
//                 result.rejectValue("email", null, "There is already an account registered with that email");
//             }
//         }

//         if (!user.getUsername().equals(oldUser.getUsername())) {
//             if (userRepository.existsByUsername(user.getUsername())) {
//                 result.rejectValue("username", null, "There is already an account registered with that username");
//             }
//         }

//         if (!user.getPhone().equals(oldUser.getPhone())) {
//             if (userRepository.existsByPhone(user.getPhone())) {
//                 result.rejectValue("phone", null, "There is already an account registered with that phone number");
//             }
//         }

//         if (!user.getIdentityCard().equals(oldUser.getIdentityCard())) {
//             if (userRepository.existsByIdentityCard(user.getIdentityCard())) {
//                 result.rejectValue("identityCard", null, "There is already an account registered with that identity card");
//             }
//         }

//         if (result.hasErrors()) {
//             result.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
//             model.addAttribute("user", user);
//             return "admin/employee/update";
//         }

//         userRepository.save(user);
//         return "redirect:/admin/employee/list";
//     }

//     @GetMapping("/admin/employee/list")
//     public String listEmployee( @RequestParam(defaultValue = "0") int page,
//                                 @RequestParam(defaultValue = "5") int size,
//                                 @RequestParam(defaultValue = "") String keyword,
//                                 Model model) {
//         Page<Users> posts = userRepository.searchEmployee(keyword, PageRequest.of(page, size));

//         int totalPages = posts.getTotalPages();

//         if (totalPages > 0) {
//             int start = Math.max(1, page - 1);
//             int end = Math.min(totalPages, page + 3 );

//             if (end - start < 4) {
//                 if (start == 1) {
//                     end = Math.min(totalPages, start + 4);
//                 } else if (end == totalPages) {
//                     start = Math.max(1, end - 4);
//                 }
//             }

//             List<Integer> pageNumbers = IntStream.rangeClosed(start, end)
//                     .boxed()
//                     .collect(Collectors.toList());
//             model.addAttribute("pageNumbers", pageNumbers);
//         }

//         List<Integer> pageNums = new ArrayList<>();
//         for (int i = 1; i <= posts.getTotalPages(); i++) {
//             pageNums.add(i);
//         }
//         model.addAttribute("posts", posts);
//         model.addAttribute("size", size);
//         model.addAttribute("keyword", keyword);

//         return "admin/employee/list";
//     }





// }
