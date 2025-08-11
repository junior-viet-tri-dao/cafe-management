package com.viettridao.cafe.controller; // Khai báo gói cho lớp ProductController

import com.viettridao.cafe.dto.request.product.CreateProductRequest; // Nhập lớp CreateProductRequest từ gói DTO
import com.viettridao.cafe.dto.request.product.UpdateProductRequest; // Nhập lớp UpdateProductRequest từ gói DTO
import com.viettridao.cafe.model.ProductEntity; // Nhập lớp ProductEntity từ gói model
import com.viettridao.cafe.repository.ProductRepository; // Nhập giao diện ProductRepository từ gói repository
import com.viettridao.cafe.repository.UnitRepository; // Nhập giao diện UnitRepository từ gói repository
import com.viettridao.cafe.service.ProductService; // Nhập giao diện ProductService từ gói service
import jakarta.validation.Valid; // Nhập chú thích Valid của Jakarta Validation để kiểm tra hợp lệ
import lombok.RequiredArgsConstructor; // Nhập chú thích RequiredArgsConstructor của Lombok để tạo constructor
import org.springframework.stereotype.Controller; // Nhập chú thích Controller của Spring để đánh dấu lớp là một controller
import org.springframework.ui.Model; // Nhập lớp Model của Spring để truyền dữ liệu đến view
import org.springframework.validation.BindingResult; // Nhập lớp BindingResult để kiểm tra lỗi ràng buộc dữ liệu
import org.springframework.web.bind.annotation.*; // Nhập tất cả các chú thích từ gói web.bind.annotation (ví dụ: GetMapping, PostMapping, RequestMapping, PathVariable, ModelAttribute)

/**
 * ProductController
 *
 * Lớp này xử lý các yêu cầu liên quan đến quản lý sản phẩm,
 * bao gồm hiển thị danh sách sản phẩm, thêm mới, cập nhật và xóa sản phẩm.
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
@RequestMapping("/product") // Ánh xạ tất cả các yêu cầu bắt đầu bằng "/product" đến controller này
public class ProductController {

    /**
     * Tiêm phụ thuộc ProductRepository để truy cập dữ liệu sản phẩm.
     */
    private final ProductRepository productRepository; // Khai báo một trường final cho ProductRepository

    /**
     * Tiêm phụ thuộc ProductService để xử lý logic nghiệp vụ liên quan đến sản phẩm.
     */
    private final ProductService productService; // Khai báo một trường final cho ProductService

    /**
     * Tiêm phụ thuộc UnitRepository để truy cập dữ liệu đơn vị.
     */
    private final UnitRepository unitRepository; // Khai báo một trường final cho UnitRepository

    /**
     * Hiển thị danh sách sản phẩm với phân trang và tìm kiếm.
     * Xử lý yêu cầu GET đến "/product".
     *
     * @param keyword Từ khóa để tìm kiếm sản phẩm (tùy chọn).
     * @param page Số trang hiện tại (mặc định là 0).
     * @param size Kích thước trang (số lượng mục trên mỗi trang, mặc định là 5).
     * @param model Đối tượng Model để truyền dữ liệu đến view.
     * @return Tên của view (trang HTML) hiển thị danh sách sản phẩm.
     */
    @GetMapping("") // Ánh xạ yêu cầu GET đến đường dẫn gốc của "/product"
    public String show_list_product(@RequestParam(required = false) String keyword, // Lấy tham số keyword từ URL, không bắt buộc
                                    @RequestParam(defaultValue = "0") int page, // Lấy tham số page từ URL, mặc định là 0
                                    @RequestParam(defaultValue = "5") int size, // Lấy tham số size từ URL, mặc định là 5
                                    Model model) { // Đối tượng model để thêm thuộc tính cho view
        // Lấy danh sách sản phẩm đã phân trang và lọc theo từ khóa
        model.addAttribute("products", productService.getAllProductPage(keyword, page, size));
        return "product/list_product"; // Trả về tên của view "product/list_product.html"
    }

    /**
     * Hiển thị biểu mẫu để chèn một sản phẩm mới.
     * Xử lý yêu cầu GET đến "/product/insert".
     *
     * @param model Đối tượng Model để truyền dữ liệu đến view.
     * @return Tên của view (trang HTML) cho biểu mẫu chèn sản phẩm.
     */
    @GetMapping("/insert") // Ánh xạ yêu cầu GET đến "/product/insert"
    public String show_form_insert_product(Model model) { // Định nghĩa phương thức để hiển thị biểu mẫu chèn sản phẩm
        model.addAttribute("product", new CreateProductRequest()); // Thêm một đối tượng CreateProductRequest mới vào model
        model.addAttribute("units", unitRepository.findAll()); // Thêm danh sách tất cả đơn vị vào model
        return "product/insert_product"; // Trả về tên của view "product/insert_product.html"
    }

    /**
     * Xử lý yêu cầu tạo một sản phẩm mới.
     * Xử lý yêu cầu POST đến "/product/insert".
     *
     * @param request Đối tượng CreateProductRequest chứa thông tin sản phẩm cần tạo.
     * @param result Đối tượng BindingResult để kiểm tra lỗi ràng buộc dữ liệu.
     * @return Chuỗi chuyển hướng đến trang danh sách sản phẩm nếu thành công, hoặc quay lại biểu mẫu chèn nếu có lỗi.
     */
    @PostMapping("/insert") // Ánh xạ yêu cầu POST đến "/product/insert"
    public String create_product(@Valid @ModelAttribute("product") CreateProductRequest request, BindingResult result) { // Định nghĩa phương thức để tạo sản phẩm
        if (result.hasErrors()) { // Kiểm tra xem có lỗi ràng buộc dữ liệu nào không
            return "product/insert_product"; // Nếu có lỗi, trả về tên của view "product/insert_product.html" để hiển thị lỗi
        }

        productService.createProduct(request); // Gọi dịch vụ để tạo sản phẩm mới

        return "redirect:/product"; // Chuyển hướng người dùng đến trang "/product" sau khi tạo thành công
    }

    /**
     * Xử lý yêu cầu xóa một sản phẩm.
     * Xử lý yêu cầu GET đến "/product/delete/{id}".
     *
     * @param id ID của sản phẩm cần xóa.
     * @return Chuỗi chuyển hướng đến trang danh sách sản phẩm sau khi xóa.
     */
    @GetMapping("/delete/{id}") // Ánh xạ yêu cầu GET đến "/product/delete/{id}"
    public String delete_product(@PathVariable("id") Integer id) { // Định nghĩa phương thức để xóa sản phẩm
        productRepository.deleteById(id); // Gọi repository để xóa sản phẩm theo ID
        return "redirect:/product"; // Chuyển hướng người dùng đến trang "/product" sau khi xóa thành công
    }

    /**
     * Hiển thị biểu mẫu cập nhật sản phẩm.
     * Xử lý yêu cầu GET đến "/product/edit/{id}".
     *
     * @param id ID của sản phẩm cần cập nhật.
     * @param model Đối tượng Model để truyền dữ liệu đến view.
     * @return Tên của view (trang HTML) cho biểu mẫu cập nhật sản phẩm.
     */
    @GetMapping("/edit/{id}") // Ánh xạ yêu cầu GET đến "/product/edit/{id}"
    public String show_form_edit_product(@PathVariable("id") Integer id, Model model) { // Định nghĩa phương thức để hiển thị biểu mẫu chỉnh sửa sản phẩm
        ProductEntity productEntity = productService.getProductById(id); // Lấy đối tượng sản phẩm theo ID
        model.addAttribute("product", productEntity); // Thêm đối tượng sản phẩm vào model để truyền đến view
        return "product/edit_product"; // Trả về tên của view "product/edit_product.html"
    }

    /**
     * Xử lý yêu cầu cập nhật sản phẩm.
     * Xử lý yêu cầu POST đến "/product/edit".
     *
     * @param request Đối tượng UpdateProductRequest chứa thông tin sản phẩm cần cập nhật.
     * @param result Đối tượng BindingResult để kiểm tra lỗi ràng buộc dữ liệu.
     * @return Chuỗi chuyển hướng đến trang danh sách sản phẩm nếu thành công, hoặc quay lại biểu mẫu chỉnh sửa nếu có lỗi.
     */
    @PostMapping("/edit") // Ánh xạ yêu cầu POST đến "/product/edit"
    public String update_product(@Valid @ModelAttribute("product") UpdateProductRequest request, BindingResult result) { // Định nghĩa phương thức để cập nhật sản phẩm
        if (result.hasErrors()) { // Kiểm tra xem có lỗi ràng buộc dữ liệu nào không
            return "product/edit_product"; // Nếu có lỗi, trả về tên của view "product/edit_product.html" để hiển thị lỗi
        }

        productService.updateProduct(request); // Gọi dịch vụ để cập nhật sản phẩm

        return "redirect:/product"; // Chuyển hướng người dùng đến trang "/product" sau khi cập nhật thành công
    }
}
