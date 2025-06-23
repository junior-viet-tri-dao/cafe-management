package com.viettridao.cafe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.Service.NhanVienService;
import com.viettridao.cafe.model.NhanVien;

@Controller
@RequestMapping("/nhanvien")
public class NhanVienController {

    @Autowired
    private NhanVienService service;

    // ✅ Xóa nhân viên có kiểm tra vai trò
    @PostMapping("/xoa")
    public String xoa(@RequestParam String username, RedirectAttributes redirect) {
        String kq = service.xoaNhanVien(username); // gọi đúng biến

        if (kq.contains("không thể") || kq.contains("Không")) {
            redirect.addFlashAttribute("error", kq);
        } else {
            redirect.addFlashAttribute("success", kq);
        }

        return "redirect:/nhanvien";
    }

    @GetMapping("")
    public String listNhanVien(Model model) {
        List<NhanVien> list = service.findAll();
        model.addAttribute("list", list);
        return "QuanLyNhanVien/nhanvien_list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("nv", new NhanVien());
        model.addAttribute("dschucvu", List.of(
            "Chủ quán", "Quản lý", "Quản lý ca", "Pha chế",
            "Nhân viên phục vụ", "Thu ngân", "Nhân viên bếp",
            "Nhân viên tạp vụ", "Nhân viên marketing"
        ));
        return "QuanLyNhanVien/nhanvien_add";
    }

    @PostMapping("/add")
    public String addNhanVien(@ModelAttribute("nv") NhanVien nv, Model model) {
        if (service.existsByUser(nv.getUser())) {
            model.addAttribute("error", "Tên đăng nhập đã tồn tại!");
            model.addAttribute("dschucvu", List.of(
                "Chủ quán", "Quản lý", "Quản lý ca", "Pha chế",
                "Nhân viên phục vụ", "Thu ngân", "Nhân viên bếp",
                "Nhân viên tạp vụ", "Nhân viên marketing"
            ));
            return "QuanLyNhanVien/nhanvien_add";
        }

        service.save(nv);
        return "redirect:/nhanvien";
    }

    @GetMapping("/edit/{user}")
    public String editForm(@PathVariable String user, Model model) {
        NhanVien nv = service.findById(user);
        if (nv == null)
            return "redirect:/nhanvien";

        model.addAttribute("nv", nv);
        model.addAttribute("dschucvu", List.of(
            "Chủ quán", "Quản lý", "Quản lý ca", "Pha chế",
            "Nhân viên phục vụ", "Thu ngân", "Nhân viên bếp",
            "Nhân viên tạp vụ", "Nhân viên marketing"
        ));
        return "QuanLyNhanVien/nhanvien_edit";
    }

    @PostMapping("/edit")
    public String updateNhanVien(@ModelAttribute("nv") NhanVien nv) {
        service.save(nv);
        return "redirect:/nhanvien";
    }

    @GetMapping("/delete/{user}")
    public String delete(@PathVariable String user) {
        service.delete(user); // xóa mềm
        return "redirect:/nhanvien";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String keyword, Model model) {
        List<NhanVien> list = service.search(keyword);
        model.addAttribute("list", list);
        return "QuanLyNhanVien/nhanvien_list";
    }
}
