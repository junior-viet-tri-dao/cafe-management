package com.viettridao.cafe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.viettridao.cafe.controller.Request.EquipmentRequest;
import com.viettridao.cafe.controller.response.EquipmentResponse;
import com.viettridao.cafe.model.EquipmentEntity;
import com.viettridao.cafe.service.EquipmentService;

import jakarta.validation.Valid;

/**
 * Controller xử lý các yêu cầu liên quan đến quản lý thiết bị (Equipment).
 * Hỗ trợ các chức năng hiển thị danh sách, thêm, sửa, xóa thiết bị.
 */
@Controller
@RequestMapping("/equipment")
public class EquipmentController {

    // Dịch vụ thiết bị, được tiêm phụ thuộc tự động
    @Autowired
    private EquipmentService equipmentService;

    /**
     * Chuyển hướng từ /equipment/list sang /equipment.
     * 
     * @return Đường dẫn chuyển hướng
     */
    @GetMapping("/list")
    public String redirectToList() {
        return "redirect:/equipment";
    }

    /**
     * Hiển thị danh sách các thiết bị đang hoạt động.
     * 
     * @param model Đối tượng Model để truyền dữ liệu tới view
     * @return Tên view hiển thị danh sách thiết bị
     */
    @GetMapping
    public String list(Model model) {
        try {
            // Lấy danh sách thiết bị đang hoạt động
            List<EquipmentResponse> list = equipmentService.getAllActiveAsResponse();
            // Thêm danh sách vào model để hiển thị trong view
            model.addAttribute("equipments", list);
        } catch (Exception e) {
            // Thêm thông báo lỗi nếu truy vấn thất bại
            model.addAttribute("error", "Không thể tải danh sách thiết bị: " + e.getMessage());
        }
        return "equipment/list";
    }

    /**
     * Hiển thị form thêm thiết bị mới.
     * 
     * @param model Đối tượng Model để truyền dữ liệu tới view
     * @return Tên view hiển thị form thêm
     */
    @GetMapping("/add")
    public String addForm(Model model) {
        // Khởi tạo EquipmentRequest mới và thêm vào model
        model.addAttribute("request", new EquipmentRequest());
        return "equipment/add";
    }

    /**
     * Xử lý submit form thêm thiết bị mới.
     * 
     * @param request Đối tượng EquipmentRequest chứa dữ liệu form
     * @param result Đối tượng BindingResult chứa kết quả validation
     * @param model Đối tượng Model để truyền dữ liệu tới view
     * @return Đường dẫn chuyển hướng hoặc tên view nếu có lỗi
     */
    @PostMapping("/add")
    public String addSubmit(@Valid @ModelAttribute("request") EquipmentRequest request, BindingResult result,
            Model model) {
        // Kiểm tra lỗi validation
        if (result.hasErrors()) {
            return "equipment/add";
        }

        try {
            // Gọi dịch vụ để tạo thiết bị mới
            equipmentService.create(request);
            // Chuyển hướng về danh sách thiết bị
            return "redirect:/equipment";
        } catch (Exception e) {
            // Thêm thông báo lỗi nếu thêm thất bại
            model.addAttribute("error", "Thêm thiết bị thất bại: " + e.getMessage());
            return "equipment/add";
        }
    }

    /**
     * Hiển thị form sửa thông tin thiết bị.
     * 
     * @param id ID của thiết bị cần sửa
     * @param model Đối tượng Model để truyền dữ liệu tới view
     * @return Tên view hiển thị form sửa hoặc chuyển hướng nếu lỗi
     */
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        try {
            // Lấy thông tin thiết bị theo ID
            EquipmentEntity equipment = equipmentService.getById(id);
            // Chuyển đổi sang EquipmentRequest để bind vào form
            EquipmentRequest request = convertToRequest(equipment);
            // Thêm request vào model
            model.addAttribute("request", request);
            return "equipment/edit";
        } catch (Exception e) {
            // Thêm thông báo lỗi và chuyển hướng về danh sách nếu không tìm thấy
            model.addAttribute("error", "Không tìm thấy thiết bị: " + e.getMessage());
            return "redirect:/equipment";
        }
    }

    /**
     * Xử lý submit form sửa thiết bị.
     * 
     * @param request Đối tượng EquipmentRequest chứa dữ liệu form
     * @param result Đối tượng BindingResult chứa kết quả validation
     * @param model Đối tượng Model để truyền dữ liệu tới view
     * @return Đường dẫn chuyển hướng hoặc tên view nếu có lỗi
     */
    @PostMapping("/edit")
    public String editSubmit(@Valid @ModelAttribute("request") EquipmentRequest request, BindingResult result,
            Model model) {
        // Kiểm tra lỗi validation
        if (result.hasErrors()) {
            return "equipment/edit";
        }

        try {
            // Gọi dịch vụ để cập nhật thiết bị
            equipmentService.update(request);
            // Chuyển hướng về danh sách thiết bị
            return "redirect:/equipment";
        } catch (Exception e) {
            // Thêm thông báo lỗi nếu cập nhật thất bại
            model.addAttribute("error", "Cập nhật thất bại: " + e.getMessage());
            return "equipment/edit";
        }
    }

    /**
     * Xóa thiết bị (xóa mềm) theo ID.
     * 
     * @param id ID của thiết bị cần xóa
     * @param model Đối tượng Model để truyền dữ liệu tới view
     * @return Đường dẫn chuyển hướng về danh sách thiết bị
     */
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, Model model) {
        try {
            // Gọi dịch vụ để xóa thiết bị
            equipmentService.delete(id);
        } catch (Exception e) {
            // Thêm thông báo lỗi nếu xóa thất bại
            model.addAttribute("error", "Xóa thiết bị thất bại: " + e.getMessage());
        }
        // Chuyển hướng về danh sách thiết bị
        return "redirect:/equipment";
    }

    /**
     * Chuyển đổi từ EquipmentEntity sang EquipmentRequest để sử dụng trong form sửa.
     * 
     * @param e Đối tượng EquipmentEntity cần chuyển đổi
     * @return Đối tượng EquipmentRequest chứa thông tin tương ứng
     */
    private EquipmentRequest convertToRequest(EquipmentEntity e) {
        EquipmentRequest r = new EquipmentRequest();
        // Gán các thuộc tính từ entity sang request
        r.setId(e.getId());
        r.setEquipmentName(e.getEquipmentName());
        r.setPurchaseDate(e.getPurchaseDate());
        r.setQuantity(e.getQuantity());
        r.setPurchasePrice(e.getPurchasePrice());
        r.setNotes(e.getNotes());
        return r;
    }
}