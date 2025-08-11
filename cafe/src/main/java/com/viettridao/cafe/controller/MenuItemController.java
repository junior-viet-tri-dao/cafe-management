package com.viettridao.cafe.controller; // Khai báo gói cho lớp MenuItemController

import com.viettridao.cafe.dto.request.menu.CreateMenuItemRequest; // Nhập lớp CreateMenuItemRequest từ gói DTO
import com.viettridao.cafe.dto.request.menu.UpdateMenuItemRequest; // Nhập lớp UpdateMenuItemRequest từ gói DTO
import com.viettridao.cafe.model.MenuDetailEntity; // Nhập lớp MenuDetailEntity từ gói model
import com.viettridao.cafe.model.MenuItemEntity; // Nhập lớp MenuItemEntity từ gói model
import com.viettridao.cafe.service.MenuItemService; // Nhập giao diện MenuItemService từ gói service
import com.viettridao.cafe.service.ProductService; // Nhập giao diện ProductService từ gói service
import com.viettridao.cafe.service.UnitService; // Nhập giao diện UnitService từ gói service
import jakarta.validation.Valid; // Nhập chú thích Valid của Jakarta Validation để kiểm tra hợp lệ
import lombok.RequiredArgsConstructor; // Nhập chú thích RequiredArgsConstructor của Lombok để tạo constructor
import org.springframework.stereotype.Controller; // Nhập chú thích Controller của Spring để đánh dấu lớp là một controller
import org.springframework.ui.Model; // Nhập lớp Model của Spring để truyền dữ liệu đến view
import org.springframework.validation.BindingResult; // Nhập lớp BindingResult để kiểm tra lỗi ràng buộc dữ liệu
import org.springframework.web.bind.annotation.*; // Nhập tất cả các chú thích từ gói web.bind.annotation (ví dụ: GetMapping, PostMapping, RequestMapping, PathVariable, ModelAttribute)

import java.util.List; // Nhập lớp List từ gói java.util

/**
 * MenuItemController
 *
 * Lớp này xử lý các yêu cầu liên quan đến quản lý các mục trong menu,
 * bao gồm hiển thị danh sách, thêm mới, cập nhật và xóa các mục menu.
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
 * 2025-07-23          Hoa Mãn Lâu     Tạo và định dạng ban đầu.
 */
@Controller // Đánh dấu lớp này là một Spring MVC Controller
@RequiredArgsConstructor // Tự động tạo constructor với các trường final
@RequestMapping("/menu") // Ánh xạ tất cả các yêu cầu bắt đầu bằng "/menu" đến controller này
public class MenuItemController {

    /**
     * Tiêm phụ thuộc MenuItemService để xử lý logic nghiệp vụ liên quan đến mục menu.
     */
    private final MenuItemService menuItemService; // Khai báo một trường final cho MenuItemService

    /**
     * Tiêm phụ thuộc ProductService để xử lý logic nghiệp vụ liên quan đến sản phẩm.
     */
    private final ProductService productService; // Khai báo một trường final cho ProductService

    /**
     * Tiêm phụ thuộc UnitService để xử lý logic nghiệp vụ liên quan đến đơn vị.
     */
    private final UnitService unitService; // Khai báo một trường final cho UnitService

    /**
     * Hiển thị danh sách các mục menu với phân trang và tìm kiếm.
     * Xử lý yêu cầu GET đến "/menu".
     *
     * @param keyword Từ khóa để tìm kiếm mục menu (tùy chọn).
     * @param page Số trang hiện tại (mặc định là 0).
     * @param size Kích thước trang (số lượng mục trên mỗi trang, mặc định là 5).
     * @param model Đối tượng Model để truyền dữ liệu đến view.
     * @return Tên của view (trang HTML) hiển thị danh sách mục menu.
     */
    @GetMapping("") // Ánh xạ yêu cầu GET đến đường dẫn gốc của "/menu"
    public String showListMenu(@RequestParam(required = false) String keyword, // Lấy tham số keyword từ URL, không bắt buộc
                               @RequestParam(defaultValue = "0") int page, // Lấy tham số page từ URL, mặc định là 0
                               @RequestParam(defaultValue = "5") int size, // Lấy tham số size từ URL, mặc định là 5
                               Model model) { // Đối tượng model để thêm thuộc tính cho view
        // Lấy danh sách mục menu đã phân trang và lọc theo từ khóa
        model.addAttribute("MenuItems", menuItemService.getAllMenuItemPage(keyword, page, size));
        return "/menu/list_menu"; // Trả về tên của view "menu/list_menu.html"
    }

    /**
     * Hiển thị biểu mẫu để chèn một mục menu mới.
     * Xử lý yêu cầu GET đến "/menu/insert".
     *
     * @param model Đối tượng Model để truyền dữ liệu đến view.
     * @return Tên của view (trang HTML) cho biểu mẫu chèn mục menu.
     */
    @GetMapping("/insert") // Ánh xạ yêu cầu GET đến "/menu/insert"
    public String showFormInsertMenu(Model model) { // Định nghĩa phương thức để hiển thị biểu mẫu chèn mục menu
        model.addAttribute("MenuItem", new CreateMenuItemRequest()); // Thêm một đối tượng CreateMenuItemRequest mới vào model
        model.addAttribute("products", productService.getAllProduct()); // Thêm danh sách tất cả sản phẩm vào model
        model.addAttribute("Units", unitService.getAllUnit()); // Thêm danh sách tất cả đơn vị vào model
        return "/menu/insert_menu"; // Trả về tên của view "menu/insert_menu.html"
    }

    /**
     * Xử lý yêu cầu chèn một mục menu mới.
     * Xử lý yêu cầu POST đến "/menu/insert".
     *
     * @param createMenuRequest Đối tượng CreateMenuItemRequest chứa thông tin mục menu cần tạo.
     * @param result Đối tượng BindingResult để kiểm tra lỗi ràng buộc dữ liệu.
     * @return Chuỗi chuyển hướng đến trang danh sách mục menu nếu thành công, hoặc quay lại biểu mẫu chèn nếu có lỗi.
     */
    @PostMapping("/insert") // Ánh xạ yêu cầu POST đến "/menu/insert"
    public String insertMenu(@Valid @ModelAttribute("MenuItem") CreateMenuItemRequest createMenuRequest, BindingResult result) { // Định nghĩa phương thức để chèn mục menu
        if (result.hasErrors()) { // Kiểm tra xem có lỗi ràng buộc dữ liệu nào không
            return "redirect:/menu"; // Nếu có lỗi, chuyển hướng lại trang "/menu"
        }

        menuItemService.createMenu(createMenuRequest); // Gọi dịch vụ để tạo mục menu mới

        return "redirect:/menu"; // Chuyển hướng người dùng đến trang "/menu" sau khi tạo thành công
    }

    // Các phương thức comment out cho chức năng chỉnh sửa mục menu
    // @GetMapping("/edit/{id}")
    // public String showFormEditMenu(@PathVariable("id") Integer id, Model model){
    // MenuItemEntity menuItemEntity = menuItemService.getMenuItemByID(id);
    // model.addAttribute("units", unitService.getAllUnit() );
    // model.addAttribute("products", productService.getAllProduct());
    // model.addAttribute("MenuItem", menuItemEntity);
    // return "/menu/edit";
    // }
    // @PostMapping("edit")
    // public String editMenuItem(@Valid @ModelAttribute("MenuItem") UpdateMenuItemRequest request, BindingResult result){
    // if(result.hasErrors()){
    // return "/menu";
    // }
    // menuItemService.updateMenuItem(request);
    // return "/menu";
    // }

    /**
     * Xử lý yêu cầu xóa một mục menu.
     * Xử lý yêu cầu GET đến "/menu/delete/{id}".
     *
     * @param id ID của mục menu cần xóa.
     * @param model Đối tượng Model (không được sử dụng trong phương thức này nhưng có thể được dùng cho các mục đích khác).
     * @return Chuỗi chuyển hướng đến trang danh sách mục menu sau khi xóa.
     */
    @GetMapping("/delete/{id}") // Ánh xạ yêu cầu GET đến "/menu/delete/{id}"
    public String deleteMenuItem(@PathVariable("id") Integer id, Model model) { // Định nghĩa phương thức để xóa mục menu
        menuItemService.deleteMenuItemById(id); // Gọi dịch vụ để xóa mục menu theo ID

        return "redirect:/menu"; // Chuyển hướng người dùng đến trang "/menu" sau khi xóa thành công
    }
}
