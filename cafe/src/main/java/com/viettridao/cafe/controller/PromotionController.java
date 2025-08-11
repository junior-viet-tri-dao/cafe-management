package com.viettridao.cafe.controller; // Khai báo gói cho lớp PromotionController

import com.viettridao.cafe.dto.request.promotion.CreatePromotionRequest; // Nhập lớp CreatePromotionRequest từ gói DTO
import com.viettridao.cafe.dto.request.promotion.UpdatePromotionRequest; // Nhập lớp UpdatePromotionRequest từ gói DTO
import com.viettridao.cafe.model.PromotionEntity; // Nhập lớp PromotionEntity từ gói model
import com.viettridao.cafe.service.PromotionService; // Nhập giao diện PromotionService từ gói service
import jakarta.validation.Valid; // Nhập chú thích Valid của Jakarta Validation để kiểm tra hợp lệ
import lombok.RequiredArgsConstructor; // Nhập chú thích RequiredArgsConstructor của Lombok để tạo constructor
import org.springframework.stereotype.Controller; // Nhập chú thích Controller của Spring để đánh dấu lớp là một controller
import org.springframework.ui.Model; // Nhập lớp Model của Spring để truyền dữ liệu đến view
import org.springframework.validation.BindingResult; // Nhập lớp BindingResult để kiểm tra lỗi ràng buộc dữ liệu
import org.springframework.web.bind.annotation.*; // Nhập tất cả các chú thích từ gói web.bind.annotation (ví dụ: GetMapping, PostMapping, RequestMapping, PathVariable, ModelAttribute)

/**
 * PromotionController
 *
 * Lớp này xử lý các yêu cầu liên quan đến quản lý khuyến mãi,
 * bao gồm hiển thị danh sách khuyến mãi, thêm mới, cập nhật và xóa khuyến mãi.
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
@RequestMapping("/promotion") // Ánh xạ tất cả các yêu cầu bắt đầu bằng "/promotion" đến controller này
public class PromotionController {

    /**
     * Tiêm phụ thuộc PromotionService để xử lý logic nghiệp vụ liên quan đến khuyến mãi.
     */
    private final PromotionService promotionService; // Khai báo một trường final cho PromotionService

    // Phương thức comment out để hiển thị danh sách khuyến mãi không phân trang
    // @GetMapping("")
    // public String list_promotion(Model model){
    // model.addAttribute("promotions",promotionService.getAllPromotion());
    // return "promotion/list_promotion";
    // }

    /**
     * Hiển thị danh sách khuyến mãi với phân trang và tìm kiếm theo tên khuyến mãi.
     * Xử lý yêu cầu GET đến "/promotion".
     *
     * @param namePromotion Tên khuyến mãi để tìm kiếm (tùy chọn).
     * @param page Số trang hiện tại (mặc định là 0).
     * @param size Kích thước trang (số lượng mục trên mỗi trang, mặc định là 5).
     * @param model Đối tượng Model để truyền dữ liệu đến view.
     * @return Tên của view (trang HTML) hiển thị danh sách khuyến mãi.
     */
    @GetMapping("") // Ánh xạ yêu cầu GET đến đường dẫn gốc của "/promotion"
    public String list_promotion(@RequestParam(required = false) String namePromotion, // Lấy tham số namePromotion từ URL, không bắt buộc
                                 @RequestParam(defaultValue = "0") int page, // Lấy tham số page từ URL, mặc định là 0
                                 @RequestParam(defaultValue = "5") int size, // Lấy tham số size từ URL, mặc định là 5
                                 Model model) { // Đối tượng model để thêm thuộc tính cho view
        // Lấy danh sách khuyến mãi đã phân trang và lọc theo tên khuyến mãi
        model.addAttribute("promotions", promotionService.getAllPromotionPage(namePromotion, page, size));
        return "promotion/list_promotion"; // Trả về tên của view "promotion/list_promotion.html"
    }

    /**
     * Xử lý yêu cầu xóa một khuyến mãi.
     * Xử lý yêu cầu GET đến "/promotion/delete/{id}".
     *
     * @param id ID của khuyến mãi cần xóa.
     * @return Chuỗi chuyển hướng đến trang danh sách khuyến mãi sau khi xóa.
     */
    @GetMapping("/delete/{id}") // Ánh xạ yêu cầu GET đến "/promotion/delete/{id}"
    public String delete_Promomotion(@PathVariable("id") Integer id) { // Định nghĩa phương thức để xóa khuyến mãi
        promotionService.deletePromotion(id); // Gọi dịch vụ để xóa khuyến mãi theo ID
        return "redirect:/promotion"; // Chuyển hướng người dùng đến trang "/promotion" sau khi xóa thành công
    }

    /**
     * Hiển thị biểu mẫu để chèn một khuyến mãi mới.
     * Xử lý yêu cầu GET đến "/promotion/insert".
     *
     * @param model Đối tượng Model để truyền dữ liệu đến view.
     * @return Tên của view (trang HTML) cho biểu mẫu chèn khuyến mãi.
     */
    @GetMapping("/insert") // Ánh xạ yêu cầu GET đến "/promotion/insert"
    public String show_form_Promomotion(Model model) { // Định nghĩa phương thức để hiển thị biểu mẫu chèn khuyến mãi
        model.addAttribute("promotion", new CreatePromotionRequest()); // Thêm một đối tượng CreatePromotionRequest mới vào model
        return "promotion/insert_promotion"; // Trả về tên của view "promotion/insert_promotion.html"
    }

    /**
     * Xử lý yêu cầu chèn một khuyến mãi mới.
     * Xử lý yêu cầu POST đến "/promotion/insert".
     *
     * @param request Đối tượng CreatePromotionRequest chứa thông tin khuyến mãi cần tạo.
     * @param bindingResult Đối tượng BindingResult để kiểm tra lỗi ràng buộc dữ liệu.
     * @return Chuỗi chuyển hướng đến trang danh sách khuyến mãi nếu thành công, hoặc quay lại biểu mẫu chèn nếu có lỗi.
     */
    @PostMapping("/insert") // Ánh xạ yêu cầu POST đến "/promotion/insert"
    public String insert_Promomotion(@Valid @ModelAttribute("promotion") CreatePromotionRequest request, BindingResult bindingResult) { // Định nghĩa phương thức để chèn khuyến mãi
        if (bindingResult.hasErrors()) { // Kiểm tra xem có lỗi ràng buộc dữ liệu nào không
            return "promotion/insert_promotion"; // Nếu có lỗi, trả về tên của view "promotion/insert_promotion.html" để hiển thị lỗi
        }

        promotionService.createPromotion(request); // Gọi dịch vụ để tạo khuyến mãi mới

        return "redirect:/promotion"; // Chuyển hướng người dùng đến trang "/promotion" sau khi tạo thành công
    }

    /**
     * Hiển thị biểu mẫu cập nhật khuyến mãi.
     * Xử lý yêu cầu GET đến "/promotion/edit/{id}".
     *
     * @param id ID của khuyến mãi cần cập nhật.
     * @param model Đối tượng Model để truyền dữ liệu đến view.
     * @return Tên của view (trang HTML) cho biểu mẫu cập nhật khuyến mãi.
     */
    @GetMapping("/edit/{id}") // Ánh xạ yêu cầu GET đến "/promotion/edit/{id}"
    public String show_form_edit_Promomotion(@PathVariable("id") Integer id, Model model) { // Định nghĩa phương thức để hiển thị biểu mẫu chỉnh sửa khuyến mãi
        PromotionEntity promotionEntity = promotionService.getPromotionById(id); // Lấy đối tượng khuyến mãi theo ID
        model.addAttribute("promotion", promotionEntity); // Thêm đối tượng khuyến mãi vào model để truyền đến view
        return "promotion/edit_promotion"; // Trả về tên của view "promotion/edit_promotion.html"
    }

    /**
     * Xử lý yêu cầu cập nhật khuyến mãi.
     * Xử lý yêu cầu POST đến "/promotion/edit".
     *
     * @param request Đối tượng UpdatePromotionRequest chứa thông tin khuyến mãi cần cập nhật.
     * @param bindingResult Đối tượng BindingResult để kiểm tra lỗi ràng buộc dữ liệu.
     * @return Chuỗi chuyển hướng đến trang danh sách khuyến mãi nếu thành công, hoặc quay lại biểu mẫu chỉnh sửa nếu có lỗi.
     */
    @PostMapping("/edit") // Ánh xạ yêu cầu POST đến "/promotion/edit"
    public String update_Promomotion(@Valid @ModelAttribute("promotion") UpdatePromotionRequest request, BindingResult bindingResult) { // Định nghĩa phương thức để cập nhật khuyến mãi
        if (bindingResult.hasErrors()) { // Kiểm tra xem có lỗi ràng buộc dữ liệu nào không
            return "promotion/edit_promotion"; // Nếu có lỗi, trả về tên của view "promotion/edit_promotion.html" để hiển thị lỗi
        }

        promotionService.updatePromotion(request); // Gọi dịch vụ để cập nhật khuyến mãi

        return "redirect:/promotion"; // Chuyển hướng người dùng đến trang "/promotion" sau khi cập nhật thành công
    }
}
