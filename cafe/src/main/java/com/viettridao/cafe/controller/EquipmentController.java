package com.viettridao.cafe.controller; // Khai báo gói cho lớp EquipmentController

import com.viettridao.cafe.dto.request.equipment.CreateEquipmentRequest; // Nhập lớp CreateEquipmentRequest từ gói DTO
import com.viettridao.cafe.dto.request.equipment.UpdateEquipmentRequest; // Nhập lớp UpdateEquipmentRequest từ gói DTO
import com.viettridao.cafe.dto.response.equipment.EquipmentResponse; // Nhập lớp EquipmentResponse từ gói DTO
import com.viettridao.cafe.mapper.EquipmentMapper; // Nhập giao diện EquipmentMapper từ gói mapper
import com.viettridao.cafe.service.EquipmentService; // Nhập giao diện EquipmentService từ gói service
import lombok.RequiredArgsConstructor; // Nhập chú thích RequiredArgsConstructor của Lombok để tạo constructor
import org.springframework.stereotype.Controller; // Nhập chú thích Controller của Spring để đánh dấu lớp là một controller
import org.springframework.ui.Model; // Nhập lớp Model của Spring để truyền dữ liệu đến view
import org.springframework.web.bind.annotation.*; // Nhập tất cả các chú thích từ gói web.bind.annotation (ví dụ: GetMapping, PostMapping, RequestMapping, PathVariable, ModelAttribute)
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Nhập lớp RedirectAttributes để truyền thuộc tính flash sau khi chuyển hướng

import java.util.List; // Nhập lớp List từ gói java.util

/**
 * EquipmentController
 *
 * Lớp này xử lý các yêu cầu liên quan đến quản lý thiết bị,
 * bao gồm hiển thị danh sách thiết bị, tạo mới, cập nhật và xóa thiết bị.
 *
 * Phiên bản 1.0
 *
 * Ngày: 2025-07-23
 *
 * Bản quyền (c) 2025 VietTriDao. Đã đăng ký bản quyền.
 *
 * Nhật ký sửa đổi:
 * NGÀY                 TÁC GIẢ          MÔ TẢ
 * -----------------------------------------------------------------------
 * 2025-07-23           Hoa Mãn Lâu     Tạo và định dạng ban đầu.
 */
@Controller // Đánh dấu lớp này là một Spring MVC Controller
@RequiredArgsConstructor // Tự động tạo constructor với các trường final
@RequestMapping("/equipment") // Ánh xạ tất cả các yêu cầu bắt đầu bằng "/equipment" đến controller này
public class EquipmentController {

    /**
     * Tiêm phụ thuộc EquipmentService để xử lý logic nghiệp vụ liên quan đến thiết bị.
     */
    private final EquipmentService equipmentService; // Khai báo một trường final cho EquipmentService

    /**
     * Tiêm phụ thuộc EquipmentMapper để ánh xạ giữa các đối tượng DTO và Entity.
     */
    private final EquipmentMapper equipmentMapper; // Khai báo một trường final cho EquipmentMapper

    /**
     * Hiển thị trang danh sách thiết bị.
     * Xử lý yêu cầu GET đến "/equipment".
     *
     * @param model Đối tượng Model để truyền dữ liệu đến view.
     * @return Tên của view (trang HTML) hiển thị danh sách thiết bị.
     */
    @GetMapping("") // Ánh xạ yêu cầu GET đến đường dẫn gốc của "/equipment"
    public String home(Model model) { // Định nghĩa phương thức để hiển thị trang chủ thiết bị
        // Lấy tất cả thiết bị từ service và ánh xạ chúng sang danh sách EquipmentResponse
        List<EquipmentResponse> equipments = equipmentMapper.toEquipmentResponseList(equipmentService.getAllEquipments());
        model.addAttribute("equiments", equipments); // Thêm danh sách thiết bị vào model để truyền đến view
        return "/equipments/equipment"; // Trả về tên của view "equipments/equipment.html"
    }

    /**
     * Hiển thị biểu mẫu tạo thiết bị mới.
     * Xử lý yêu cầu GET đến "/equipment/create".
     *
     * @return Tên của view (trang HTML) cho biểu mẫu tạo thiết bị.
     */
    @GetMapping("/create") // Ánh xạ yêu cầu GET đến "/equipment/create"
    public String showFormCreate() { // Định nghĩa phương thức để hiển thị biểu mẫu tạo thiết bị
        return "/equipments/create_equipment"; // Trả về tên của view "equipments/create_equipment.html"
    }

    /**
     * Xử lý yêu cầu tạo thiết bị mới.
     * Xử lý yêu cầu POST đến "/equipment/create".
     *
     * @param request Đối tượng CreateEquipmentRequest chứa thông tin thiết bị cần tạo.
     * @param redirectAttributes Đối tượng RedirectAttributes để thêm các thuộc tính flash cho yêu cầu chuyển hướng.
     * @return Chuỗi chuyển hướng đến trang danh sách thiết bị nếu thành công, hoặc quay lại biểu mẫu tạo nếu thất bại.
     */
    @PostMapping("/create") // Ánh xạ yêu cầu POST đến "/equipment/create"
    public String createEquipment(@ModelAttribute CreateEquipmentRequest request, RedirectAttributes redirectAttributes) { // Định nghĩa phương thức để tạo thiết bị
        try { // Bắt đầu khối try để xử lý ngoại lệ
            equipmentService.createEquipment(request); // Gọi dịch vụ để tạo thiết bị với dữ liệu từ request
            redirectAttributes.addFlashAttribute("success", "Thêm thiết bị thành công"); // Thêm thông báo thành công vào flash attributes
            return "redirect:/equipment"; // Chuyển hướng người dùng đến trang "/equipment"
        } catch (Exception e) { // Bắt các ngoại lệ có thể xảy ra
            redirectAttributes.addFlashAttribute("error", e.getMessage()); // Thêm thông báo lỗi vào flash attributes
            return "redirect:/equipment/create"; // Chuyển hướng người dùng trở lại trang "/equipment/create"
        }
    }

    /**
     * Xử lý yêu cầu xóa thiết bị.
     * Xử lý yêu cầu POST đến "/equipment/delete/{id}".
     *
     * @param id ID của thiết bị cần xóa.
     * @param redirectAttributes Đối tượng RedirectAttributes để thêm các thuộc tính flash cho yêu cầu chuyển hướng.
     * @return Chuỗi chuyển hướng đến trang danh sách thiết bị sau khi xóa.
     */
    @PostMapping("/delete/{id}") // Ánh xạ yêu cầu POST đến "/equipment/delete/{id}"
    public String deleteDevice(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) { // Định nghĩa phương thức để xóa thiết bị
        try { // Bắt đầu khối try để xử lý ngoại lệ
            equipmentService.deleteEquipment(id); // Gọi dịch vụ để xóa thiết bị theo ID
            redirectAttributes.addFlashAttribute("success", "Xoá thiết bị thành công"); // Thêm thông báo thành công vào flash attributes
            return "redirect:/equipment"; // Chuyển hướng người dùng đến trang "/equipment"
        } catch (Exception e) { // Bắt các ngoại lệ có thể xảy ra
            redirectAttributes.addFlashAttribute("error", e.getMessage()); // Thêm thông báo lỗi vào flash attributes
            return "redirect:/equipment"; // Chuyển hướng người dùng trở lại trang "/equipment"
        }
    }

    /**
     * Hiển thị biểu mẫu cập nhật thiết bị.
     * Xử lý yêu cầu GET đến "/equipment/update/{id}".
     *
     * @param id ID của thiết bị cần cập nhật.
     * @param model Đối tượng Model để truyền dữ liệu đến view.
     * @param redirectAttributes Đối tượng RedirectAttributes để thêm các thuộc tính flash cho yêu cầu chuyển hướng.
     * @return Tên của view (trang HTML) cho biểu mẫu cập nhật thiết bị nếu thành công, hoặc chuyển hướng đến trang danh sách nếu thất bại.
     */
    @GetMapping("/update/{id}") // Ánh xạ yêu cầu GET đến "/equipment/update/{id}"
    public String showFormUpdate(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) { // Định nghĩa phương thức để hiển thị biểu mẫu cập nhật
        try { // Bắt đầu khối try để xử lý ngoại lệ
            // Lấy thiết bị theo ID và ánh xạ nó sang EquipmentResponse
            EquipmentResponse response = equipmentMapper.toEquipmentResponse(equipmentService.getEquipmentById(id));
            model.addAttribute("equipment", response); // Thêm đối tượng thiết bị vào model để truyền đến view
            return "/equipments/update_equipment"; // Trả về tên của view "equipments/update_equipment.html"
        } catch (Exception e) { // Bắt các ngoại lệ có thể xảy ra
            redirectAttributes.addFlashAttribute("error", e.getMessage()); // Thêm thông báo lỗi vào flash attributes
            return "redirect:/equipment"; // Chuyển hướng người dùng trở lại trang "/equipment"
        }
    }

    /**
     * Xử lý yêu cầu cập nhật thiết bị.
     * Xử lý yêu cầu POST đến "/equipment/update".
     *
     * @param request Đối tượng UpdateEquipmentRequest chứa thông tin thiết bị cần cập nhật.
     * @param redirectAttributes Đối tượng RedirectAttributes để thêm các thuộc tính flash cho yêu cầu chuyển hướng.
     * @return Chuỗi chuyển hướng đến trang danh sách thiết bị sau khi cập nhật.
     */
    @PostMapping("/update") // Ánh xạ yêu cầu POST đến "/equipment/update"
    public String updateEquipment(@ModelAttribute UpdateEquipmentRequest request, RedirectAttributes redirectAttributes) { // Định nghĩa phương thức để cập nhật thiết bị
        try { // Bắt đầu khối try để xử lý ngoại lệ
            equipmentService.updateEquipment(request); // Gọi dịch vụ để cập nhật thiết bị với dữ liệu từ request
            redirectAttributes.addFlashAttribute("success", "Chỉnh sửa thiết bị thành công"); // Thêm thông báo thành công vào flash attributes
            return "redirect:/equipment"; // Chuyển hướng người dùng đến trang "/equipment"
        } catch (Exception e) { // Bắt các ngoại lệ có thể xảy ra
            redirectAttributes.addFlashAttribute("error", e.getMessage()); // Thêm thông báo lỗi vào flash attributes
            return "redirect:/equipment"; // Chuyển hướng người dùng trở lại trang "/equipment"
        }
    }
}
