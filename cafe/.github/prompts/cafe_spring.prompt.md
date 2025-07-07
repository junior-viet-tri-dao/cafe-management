---
mode: ask
---

# ☕ Miro Cafe - Spring Boot MVC + Thymeleaf Development Prompt

## 📋 **Tổng quan dự án**

Đây là hệ thống quản lý quán cà phê Miro Cafe được phát triển bằng Spring Boot MVC với Thymeleaf template engine và TailwindCSS. Dự án tuân thủ kiến trúc MVC pattern với server-side rendering và sử dụng MapStruct để mapping giữa các objects.

## 🏗️ **Kiến trúc và Cấu trúc dự án**

### **Package Structure (Cấu trúc thư mục)**

```
com.mirocafe
├── controller/     # MVC Controllers - Xử lý HTTP requests và trả về views
├── service/        # Business Logic Layer - Logic nghiệp vụ
├── repository/     # Data Access Layer - Truy cập cơ sở dữ liệu
├── model/          # Entity classes - Các lớp mô hình dữ liệu
├── mapper/         # MapStruct Mappers - Chuyển đổi giữa Entity và DTO
├── dto/            # Data Transfer Objects - Đối tượng truyền dữ liệu
├── config/         # Configuration classes - Cấu hình ứng dụng
├── exception/      # Custom exceptions - Xử lý ngoại lệ
└── util/           # Utility classes - Các lớp tiện ích

resources/
├── templates/      # Thymeleaf templates (.html)
│   ├── layout.html # Layout chính sử dụng TailwindCSS
│   ├── fragments/ # Các fragment dùng chung (header, footer, nav)
│   └── [entities]/ # Thư mục cho từng entity (equipments/, customers/, etc.)
│       ├── create_[entity].html  # Form tạo mới
│       ├── edit_[entity].html    # Form chỉnh sửa
│       ├── list_[entity].html    # Danh sách
│       └── detail_[entity].html  # Chi tiết
├── static/        # CSS, JS, images
│   ├── css/       # Custom CSS files
│   ├── js/        # JavaScript files
│   └── images/    # Image assets
└── application.properties
```

### **Coding Standards & Conventions**

#### **1. Controller Layer (Lớp điều khiển MVC)**

```java
@Controller
@RequestMapping("/equipment") // Sử dụng số ít cho entity
public class EquipmentController {

    private final EquipmentService equipmentService;
    private final EquipmentMapper equipmentMapper;

    // Constructor injection (bắt buộc)
    public EquipmentController(EquipmentService equipmentService, EquipmentMapper equipmentMapper) {
        this.equipmentService = equipmentService;
        this.equipmentMapper = equipmentMapper;
    }

    // Hiển thị danh sách - GET /equipment
    @GetMapping
    public String listEquipments(Model model) {
        List<Equipment> equipments = equipmentService.findAll();
        List<EquipmentDto> equipmentDtos = equipmentMapper.toDto(equipments);
        model.addAttribute("equipments", equipmentDtos);
        return "equipments/list_equipment"; // template path theo convention
    }

    // Hiển thị form tạo mới - GET /equipment/create
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("equipment", new CreateEquipmentRequest());
        return "equipments/create_equipment";
    }

    // Xử lý tạo mới - POST /equipment/create
    @PostMapping("/create")
    public String createEquipment(@Valid @ModelAttribute("equipment") CreateEquipmentRequest request,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "equipments/create_equipment";
        }

        try {
            Equipment equipment = equipmentMapper.toEntity(request);
            equipmentService.save(equipment);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm thiết bị thành công!");
            return "redirect:/equipment";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            return "equipments/create_equipment";
        }
    }

    // Hiển thị form chỉnh sửa - GET /equipment/{id}/edit
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Equipment equipment = equipmentService.findById(id);
        UpdateEquipmentRequest request = equipmentMapper.toUpdateRequest(equipment);
        model.addAttribute("equipment", request);
        model.addAttribute("equipmentId", id);
        return "equipments/edit_equipment";
    }

    // Xử lý cập nhật - POST /equipment/{id}/edit
    @PostMapping("/{id}/edit")
    public String updateEquipment(@PathVariable Long id,
                                 @Valid @ModelAttribute("equipment") UpdateEquipmentRequest request,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("equipmentId", id);
            return "equipments/edit_equipment";
        }

        try {
            Equipment equipment = equipmentMapper.toEntity(request);
            equipment.setId(id);
            equipmentService.update(equipment);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thiết bị thành công!");
            return "redirect:/equipment";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            model.addAttribute("equipmentId", id);
            return "equipments/edit_equipment";
        }
    }

    // Xóa - POST /equipment/{id}/delete
    @PostMapping("/{id}/delete")
    public String deleteEquipment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            equipmentService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa thiết bị thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
        }
        return "redirect:/equipment";
    }

    // Chi tiết - GET /equipment/{id}
    @GetMapping("/{id}")
    public String viewEquipment(@PathVariable Long id, Model model) {
        Equipment equipment = equipmentService.findById(id);
        EquipmentDto equipmentDto = equipmentMapper.toDto(equipment);
        model.addAttribute("equipment", equipmentDto);
        return "equipments/detail_equipment";
    }
}
```

#### **2. Service Layer (Lớp dịch vụ)**

```java
@Service
@Transactional
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @Transactional(readOnly = true)
    public List<Equipment> findAll() {
        return equipmentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Equipment findById(Long id) {
        return equipmentRepository.findById(id)
            .orElseThrow(() -> new EquipmentNotFoundException("Không tìm thấy thiết bị với id: " + id));
    }

    public Equipment save(Equipment equipment) {
        validateEquipment(equipment);
        return equipmentRepository.save(equipment);
    }

    public Equipment update(Equipment equipment) {
        if (!equipmentRepository.existsById(equipment.getId())) {
            throw new EquipmentNotFoundException("Không tìm thấy thiết bị với id: " + equipment.getId());
        }
        validateEquipment(equipment);
        return equipmentRepository.save(equipment);
    }

    public void delete(Long id) {
        if (!equipmentRepository.existsById(id)) {
            throw new EquipmentNotFoundException("Không tìm thấy thiết bị với id: " + id);
        }
        equipmentRepository.deleteById(id);
    }

    private void validateEquipment(Equipment equipment) {
        // Business validation logic
        if (equipment.getQuantity() <= 0) {
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        }
        if (equipment.getPurchasePrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Giá mua không được âm");
        }
        if (equipment.getPurchaseDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày mua không được trong tương lai");
        }
    }
}
```

#### **3. MapStruct Mapper Layer (Lớp chuyển đổi dữ liệu)**

```java
@Mapper(componentModel = "spring")
public interface EquipmentMapper {

    // Entity to DTO
    EquipmentDto toDto(Equipment equipment);
    List<EquipmentDto> toDto(List<Equipment> equipments);

    // Request to Entity
    Equipment toEntity(CreateEquipmentRequest request);
    Equipment toEntity(UpdateEquipmentRequest request);

    // Entity to Request (for edit form)
    CreateEquipmentRequest toCreateRequest(Equipment equipment);
    UpdateEquipmentRequest toUpdateRequest(Equipment equipment);

    // Custom mapping nếu cần
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updatedDate", ignore = true)
    Equipment toEntityForCreate(CreateEquipmentRequest request);

    // Mapping với format đặc biệt
    @Mapping(target = "formattedPrice", source = "purchasePrice", qualifiedByName = "formatPrice")
    @Mapping(target = "formattedDate", source = "purchaseDate", qualifiedByName = "formatDate")
    EquipmentDisplayDto toDisplayDto(Equipment equipment);

    @Named("formatPrice")
    default String formatPrice(BigDecimal price) {
        if (price == null) return "";
        return String.format("%,.0f VNĐ", price);
    }

    @Named("formatDate")
    default String formatDate(LocalDate date) {
        if (date == null) return "";
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
```

#### **4. DTO Classes (Lớp truyền dữ liệu)**

```java
// Response DTO
public class EquipmentDto {
    private Long id;
    private String equipmentName;
    private Integer quantity;
    private BigDecimal purchasePrice;
    private LocalDate purchaseDate;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    // Constructors, getters, setters
}

// Create Request DTO
public class CreateEquipmentRequest {
    @NotBlank(message = "Tên thiết bị không được để trống")
    @Size(min = 5, message = "Tên thiết bị phải có ít nhất 5 ký tự")
    @Pattern(regexp = "^(?!\\s*$).{5,}$", message = "Tên thiết bị không được chỉ chứa khoảng trắng")
    private String equipmentName;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private Integer quantity;

    @NotNull(message = "Đơn giá không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Đơn giá phải lớn hơn 0")
    private BigDecimal purchasePrice;

    @NotNull(message = "Ngày mua không được để trống")
    @PastOrPresent(message = "Ngày mua không được trong tương lai")
    private LocalDate purchaseDate;

    // Constructors, getters, setters
}

// Update Request DTO
public class UpdateEquipmentRequest {
    @NotBlank(message = "Tên thiết bị không được để trống")
    @Size(min = 5, message = "Tên thiết bị phải có ít nhất 5 ký tự")
    @Pattern(regexp = "^(?!\\s*$).{5,}$", message = "Tên thiết bị không được chỉ chứa khoảng trắng")
    private String equipmentName;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private Integer quantity;

    @NotNull(message = "Đơn giá không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Đơn giá phải lớn hơn 0")
    private BigDecimal purchasePrice;

    @NotNull(message = "Ngày mua không được để trống")
    @PastOrPresent(message = "Ngày mua không được trong tương lai")
    private LocalDate purchaseDate;

    // Constructors, getters, setters
}

// Display DTO (với formatted fields)
public class EquipmentDisplayDto {
    private Long id;
    private String equipmentName;
    private Integer quantity;
    private String formattedPrice;
    private String formattedDate;

    // Constructors, getters, setters
}
```

#### **5. Entity Classes (Lớp thực thể)**

```java
@Entity
@Table(name = "equipments")
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "equipment_name", nullable = false, length = 100)
    private String equipmentName;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "purchase_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "purchase_date", nullable = false)
    private LocalDate purchaseDate;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDateTime.now();
    }

    // Constructors, getters, setters, equals, hashCode, toString
}
```

#### **6. Thymeleaf Templates với TailwindCSS**

### **Layout Template (layout.html)**

```html
<!DOCTYPE html>
<html
  lang="vi"
  xmlns:th="http://www.thymeleaf.org"
  xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
>
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title th:text="${title} ?: 'Miro Cafe'">Miro Cafe</title>

    <!-- TailwindCSS CDN -->
    <script src="https://cdn.tailwindcss.com"></script>

    <!-- Custom TailwindCSS config -->
    <script>
      tailwind.config = {
        theme: {
          extend: {
            colors: {
              'cafe-bg': '#fefae0',
              'cafe-brown': '#3e2723',
              'cafe-yellow': '#facc15',
            },
          },
        },
      };
    </script>

    <!-- Custom CSS -->
    <link rel="stylesheet" th:href="@{/css/style.css}" />

    <!-- Layout head content -->
    <th:block layout:fragment="head"></th:block>
  </head>
  <body class="bg-cafe-bg min-h-screen">
    <!-- Header Fragment -->
    <header th:replace="fragments/header :: header"></header>

    <!-- Navigation Fragment -->
    <nav th:replace="fragments/navigation :: nav"></nav>

    <!-- Flash Messages -->
    <div
      th:if="${successMessage}"
      class="fixed top-4 right-4 bg-green-500 text-white px-6 py-4 rounded-lg shadow-lg z-50 transition-all duration-300"
    >
      <div class="flex items-center">
        <svg class="w-5 h-5 mr-2" fill="currentColor" viewBox="0 0 20 20">
          <path
            fill-rule="evenodd"
            d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z"
            clip-rule="evenodd"
          ></path>
        </svg>
        <span th:text="${successMessage}"></span>
      </div>
    </div>

    <div
      th:if="${errorMessage}"
      class="fixed top-4 right-4 bg-red-500 text-white px-6 py-4 rounded-lg shadow-lg z-50 transition-all duration-300"
    >
      <div class="flex items-center">
        <svg class="w-5 h-5 mr-2" fill="currentColor" viewBox="0 0 20 20">
          <path
            fill-rule="evenodd"
            d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z"
            clip-rule="evenodd"
          ></path>
        </svg>
        <span th:text="${errorMessage}"></span>
      </div>
    </div>

    <!-- Main Content -->
    <main layout:fragment="content">
      <!-- Page content goes here -->
    </main>

    <!-- Footer Fragment -->
    <footer th:replace="fragments/footer :: footer"></footer>

    <!-- Custom JS -->
    <script th:src="@{/js/script.js}"></script>

    <!-- Layout script content -->
    <th:block layout:fragment="scripts"></th:block>
  </body>
</html>
```

### **List Template với Search (equipments/list_equipment.html)**

```html
<!DOCTYPE html>
<html
  xmlns:th="http://www.thymeleaf.org"
  xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
  layout:decorate="~{layout}"
>
  <head>
    <title>Danh sách thiết bị</title>
  </head>
  <body>
    <div layout:fragment="content" class="bg-cafe-bg min-h-screen">
      <div class="container mx-auto px-4 py-8">
        <!-- Header Section -->
        <div class="flex justify-between items-center mb-8">
          <h1 class="text-4xl font-bold text-cafe-brown">Danh sách thiết bị</h1>
          <a
            th:href="@{/equipment/create}"
            class="bg-cafe-yellow hover:bg-yellow-600 text-white font-semibold px-6 py-3 rounded-lg shadow-md transition-colors duration-200 flex items-center"
          >
            <svg
              class="w-5 h-5 mr-2"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M12 6v6m0 0v6m0-6h6m-6 0H6"
              ></path>
            </svg>
            Thêm thiết bị mới
          </a>
        </div>

        <!-- Search Section -->
        <div class="bg-white rounded-lg shadow-md p-6 mb-6">
          <form th:action="@{/equipment}" method="get" class="space-y-4">
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
              <!-- Search by name -->
              <div>
                <label
                  for="search"
                  class="block text-sm font-medium text-cafe-brown mb-2"
                  >Tìm kiếm theo tên:</label
                >
                <input
                  type="text"
                  id="search"
                  name="search"
                  th:value="${search}"
                  placeholder="Nhập tên thiết bị..."
                  class="w-full border border-gray-300 rounded-md px-4 py-2 focus:ring focus:ring-cafe-yellow focus:ring-opacity-50 outline-none transition-all duration-200"
                />
              </div>

              <!-- Date range filter -->
              <div>
                <label
                  for="fromDate"
                  class="block text-sm font-medium text-cafe-brown mb-2"
                  >Từ ngày:</label
                >
                <input
                  type="date"
                  id="fromDate"
                  name="fromDate"
                  th:value="${fromDate}"
                  class="w-full border border-gray-300 rounded-md px-4 py-2 focus:ring focus:ring-cafe-yellow focus:ring-opacity-50 outline-none transition-all duration-200"
                />
              </div>

              <div>
                <label
                  for="toDate"
                  class="block text-sm font-medium text-cafe-brown mb-2"
                  >Đến ngày:</label
                >
                <input
                  type="date"
                  id="toDate"
                  name="toDate"
                  th:value="${toDate}"
                  class="w-full border border-gray-300 rounded-md px-4 py-2 focus:ring focus:ring-cafe-yellow focus:ring-opacity-50 outline-none transition-all duration-200"
                />
              </div>
            </div>

            <!-- Search buttons -->
            <div class="flex gap-4 pt-4">
              <button
                type="submit"
                class="bg-cafe-brown hover:bg-amber-800 text-white font-semibold px-6 py-2 rounded-md shadow transition-colors duration-200 flex items-center"
              >
                <svg
                  class="w-4 h-4 mr-2"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
                  ></path>
                </svg>
                Tìm kiếm
              </button>

              <a
                th:href="@{/equipment}"
                class="bg-gray-500 hover:bg-gray-600 text-white font-semibold px-6 py-2 rounded-md shadow transition-colors duration-200 flex items-center"
              >
                <svg
                  class="w-4 h-4 mr-2"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"
                  ></path>
                </svg>
                Làm mới
              </a>
            </div>
          </form>
        </div>

        <!-- Results Summary -->
        <div class="mb-4">
          <p class="text-cafe-brown font-medium">
            Tìm thấy
            <span
              class="font-bold text-cafe-yellow"
              th:text="${#lists.size(equipments)}"
              >0</span
            >
            thiết bị
            <span th:if="${search}" class="ml-2">
              cho từ khóa: "<span class="font-bold" th:text="${search}"></span>"
            </span>
          </p>
        </div>

        <!-- Equipment Table -->
        <div class="bg-white rounded-lg shadow-md overflow-hidden">
          <div class="overflow-x-auto">
            <table class="min-w-full divide-y divide-gray-200">
              <thead class="bg-cafe-brown">
                <tr>
                  <th
                    class="px-6 py-3 text-left text-xs font-medium text-white uppercase tracking-wider"
                  >
                    ID
                  </th>
                  <th
                    class="px-6 py-3 text-left text-xs font-medium text-white uppercase tracking-wider"
                  >
                    Tên thiết bị
                  </th>
                  <th
                    class="px-6 py-3 text-left text-xs font-medium text-white uppercase tracking-wider"
                  >
                    Số lượng
                  </th>
                  <th
                    class="px-6 py-3 text-left text-xs font-medium text-white uppercase tracking-wider"
                  >
                    Đơn giá
                  </th>
                  <th
                    class="px-6 py-3 text-left text-xs font-medium text-white uppercase tracking-wider"
                  >
                    Ngày mua
                  </th>
                  <th
                    class="px-6 py-3 text-left text-xs font-medium text-white uppercase tracking-wider"
                  >
                    Thao tác
                  </th>
                </tr>
              </thead>
              <tbody class="bg-white divide-y divide-gray-200">
                <!-- Empty state -->
                <tr th:if="${#lists.isEmpty(equipments)}">
                  <td colspan="6" class="px-6 py-12 text-center">
                    <div class="flex flex-col items-center">
                      <svg
                        class="w-12 h-12 text-gray-400 mb-4"
                        fill="none"
                        stroke="currentColor"
                        viewBox="0 0 24 24"
                      >
                        <path
                          stroke-linecap="round"
                          stroke-linejoin="round"
                          stroke-width="2"
                          d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4"
                        ></path>
                      </svg>
                      <p class="text-gray-500 text-lg font-medium">
                        Không có thiết bị nào
                      </p>
                      <p class="text-gray-400 text-sm mt-1">
                        Hãy thêm thiết bị mới để bắt đầu quản lý
                      </p>
                    </div>
                  </td>
                </tr>

                <!-- Equipment rows -->
                <tr
                  th:each="equipment : ${equipments}"
                  class="hover:bg-gray-50 transition-colors duration-150"
                >
                  <td
                    class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900"
                    th:text="${equipment.id}"
                  ></td>
                  <td class="px-6 py-4 whitespace-nowrap">
                    <div
                      class="text-sm font-medium text-gray-900"
                      th:text="${equipment.equipmentName}"
                    ></div>
                  </td>
                  <td
                    class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"
                    th:text="${equipment.quantity}"
                  ></td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                    <span
                      class="font-medium text-green-600"
                      th:text="${#numbers.formatDecimal(equipment.purchasePrice, 0, 'COMMA', 0, 'POINT')} + ' VNĐ'"
                    ></span>
                  </td>
                  <td
                    class="px-6 py-4 whitespace-nowrap text-sm text-gray-900"
                    th:text="${#temporals.format(equipment.purchaseDate, 'dd/MM/yyyy')}"
                  ></td>
                  <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                    <div class="flex space-x-2">
                      <!-- Edit button -->
                      <a
                        th:href="@{'/equipment/' + ${equipment.id} + '/edit'}"
                        class="bg-blue-500 hover:bg-blue-600 text-white px-3 py-1 rounded text-xs font-medium transition-colors duration-200 flex items-center"
                        title="Chỉnh sửa"
                      >
                        <svg
                          class="w-3 h-3 mr-1"
                          fill="none"
                          stroke="currentColor"
                          viewBox="0 0 24 24"
                        >
                          <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"
                          ></path>
                        </svg>
                        Sửa
                      </a>

                      <!-- Delete button -->
                      <button
                        type="button"
                        class="bg-red-500 hover:bg-red-600 text-white px-3 py-1 rounded text-xs font-medium transition-colors duration-200 flex items-center"
                        title="Xóa"
                        onclick="confirmDelete(this)"
                        th:data-url="@{'/equipment/' + ${equipment.id} + '/delete'}"
                        th:data-name="${equipment.equipmentName}"
                      >
                        <svg
                          class="w-3 h-3 mr-1"
                          fill="none"
                          stroke="currentColor"
                          viewBox="0 0 24 24"
                        >
                          <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            stroke-width="2"
                            d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
                          ></path>
                        </svg>
                        Xóa
                      </button>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <!-- Pagination -->
        <div th:if="${totalPages > 1}" class="mt-6 flex justify-center">
          <nav
            class="relative z-0 inline-flex rounded-md shadow-sm -space-x-px"
            aria-label="Pagination"
          >
            <!-- Previous page -->
            <a
              th:href="@{/equipment(page=${currentPage - 1}, search=${search}, fromDate=${fromDate}, toDate=${toDate})}"
              th:classappend="${currentPage == 0} ? 'pointer-events-none opacity-50' : ''"
              class="relative inline-flex items-center px-2 py-2 rounded-l-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50"
            >
              <span class="sr-only">Previous</span>
              <svg class="h-5 w-5" fill="currentColor" viewBox="0 0 20 20">
                <path
                  fill-rule="evenodd"
                  d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z"
                  clip-rule="evenodd"
                />
              </svg>
            </a>

            <!-- Page numbers -->
            <a
              th:each="i : ${#numbers.sequence(0, totalPages - 1)}"
              th:href="@{/equipment(page=${i}, search=${search}, fromDate=${fromDate}, toDate=${toDate})}"
              th:text="${i + 1}"
              th:classappend="${i == currentPage} ? 'bg-cafe-yellow border-cafe-yellow text-white' : 'bg-white border-gray-300 text-gray-500 hover:bg-gray-50'"
              class="relative inline-flex items-center px-4 py-2 border text-sm font-medium"
            >
            </a>

            <!-- Next page -->
            <a
              th:href="@{/equipment(page=${currentPage + 1}, search=${search}, fromDate=${fromDate}, toDate=${toDate})}"
              th:classappend="${currentPage == totalPages - 1} ? 'pointer-events-none opacity-50' : ''"
              class="relative inline-flex items-center px-2 py-2 rounded-r-md border border-gray-300 bg-white text-sm font-medium text-gray-500 hover:bg-gray-50"
            >
              <span class="sr-only">Next</span>
              <svg class="h-5 w-5" fill="currentColor" viewBox="0 0 20 20">
                <path
                  fill-rule="evenodd"
                  d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z"
                  clip-rule="evenodd"
                />
              </svg>
            </a>
          </nav>
        </div>
      </div>

      <!-- Delete Confirmation Modal -->
      <div
        id="deleteModal"
        class="hidden fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50"
      >
        <div
          class="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white"
        >
          <div class="mt-3 text-center">
            <div
              class="mx-auto flex items-center justify-center h-12 w-12 rounded-full bg-red-100 mb-4"
            >
              <svg
                class="h-6 w-6 text-red-600"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.964-.833-2.732 0L3.732 16.5C2.962 18.333 3.924 20 5.464 20z"
                ></path>
              </svg>
            </div>
            <h3 class="text-lg font-medium text-gray-900 mb-2">Xác nhận xóa</h3>
            <p class="text-sm text-gray-500 mb-1">
              Bạn có chắc chắn muốn xóa thiết bị:
            </p>
            <p
              class="text-sm font-medium text-gray-900 mb-4"
              id="deleteItemName"
            ></p>
            <p class="text-xs text-red-600">
              Hành động này không thể hoàn tác!
            </p>

            <div class="flex justify-center space-x-4 mt-6">
              <button
                type="button"
                onclick="closeDeleteModal()"
                class="bg-gray-300 hover:bg-gray-400 text-gray-800 font-medium px-4 py-2 rounded transition-colors duration-200"
              >
                Hủy
              </button>
              <form id="deleteForm" method="post" style="display: inline;">
                <button
                  type="submit"
                  class="bg-red-500 hover:bg-red-600 text-white font-medium px-4 py-2 rounded transition-colors duration-200"
                >
                  Xóa
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>

    <th:block layout:fragment="scripts">
      <script>
        function confirmDelete(button) {
          const deleteUrl = button.getAttribute('data-url');
          const itemName = button.getAttribute('data-name');

          document.getElementById('deleteItemName').textContent = itemName;
          document.getElementById('deleteForm').action = deleteUrl;
          document.getElementById('deleteModal').classList.remove('hidden');
        }

        function closeDeleteModal() {
          document.getElementById('deleteModal').classList.add('hidden');
        }

        // Close modal when clicking outside
        document
          .getElementById('deleteModal')
          .addEventListener('click', function (e) {
            if (e.target === this) {
              closeDeleteModal();
            }
          });

        // Auto-hide flash messages after 5 seconds
        setTimeout(function () {
          const successMessage = document.querySelector(
            '[th\\:if="${successMessage}"]'
          );
          const errorMessage = document.querySelector(
            '[th\\:if="${errorMessage}"]'
          );

          if (successMessage) {
            successMessage.style.opacity = '0';
            setTimeout(() => successMessage.remove(), 300);
          }
          if (errorMessage) {
            errorMessage.style.opacity = '0';
            setTimeout(() => errorMessage.remove(), 300);
          }
        }, 5000);
      </script>
    </th:block>
  </body>
</html>
```

### **Form Template Example (equipments/create_equipment.html)**

```html
<!DOCTYPE html>
<html
  xmlns:th="http://www.thymeleaf.org"
  xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
  layout:decorate="~{layout}"
>
  <head>
    <title>Thêm thiết bị</title>
  </head>
  <body>
    <div
      layout:fragment="content"
      class="bg-cafe-bg min-h-screen w-full flex justify-center items-start py-12 px-4"
    >
      <div
        class="w-full max-w-md bg-white p-10 rounded-2xl shadow-xl border border-gray-200"
      >
        <h1
          class="text-3xl font-bold text-cafe-brown mb-10 text-center tracking-wide"
        >
          Thêm thiết bị mới
        </h1>

        <form
          th:action="@{/equipment/create}"
          th:object="${equipment}"
          method="post"
          class="space-y-6"
        >
          <!-- Tên thiết bị -->
          <div>
            <label
              for="equipmentName"
              class="block text-cafe-brown font-bold text-lg mb-2"
              >* Tên thiết bị:</label
            >
            <input
              type="text"
              th:field="*{equipmentName}"
              id="equipmentName"
              required
              minlength="5"
              pattern="^(?!\s*$).{5,}$"
              title="Tên thiết bị phải có ít nhất 5 ký tự và không được để trống hoặc toàn dấu cách"
              class="w-full border border-gray-300 rounded-md px-4 py-3 text-base focus:ring focus:ring-cafe-yellow focus:ring-opacity-50 outline-none transition-all duration-200"
              th:classappend="${#fields.hasErrors('equipmentName')} ? 'border-red-500 bg-red-50' : 'border-gray-300'"
            />
            <p
              class="text-red-500 text-sm mt-1"
              th:if="${#fields.hasErrors('equipmentName')}"
              th:errors="*{equipmentName}"
            ></p>
          </div>

          <!-- Số lượng -->
          <div>
            <label
              for="quantity"
              class="block text-cafe-brown font-bold text-lg mb-2"
              >* Số lượng:</label
            >
            <input
              type="number"
              th:field="*{quantity}"
              id="quantity"
              min="1"
              required
              class="w-full border border-gray-300 rounded-md px-4 py-3 text-base focus:ring focus:ring-cafe-yellow focus:ring-opacity-50 outline-none transition-all duration-200"
              th:classappend="${#fields.hasErrors('quantity')} ? 'border-red-500 bg-red-50' : 'border-gray-300'"
            />
            <p
              class="text-red-500 text-sm mt-1"
              th:if="${#fields.hasErrors('quantity')}"
              th:errors="*{quantity}"
            ></p>
          </div>

          <!-- Đơn giá -->
          <div>
            <label
              for="purchasePrice"
              class="block text-cafe-brown font-bold text-lg mb-2"
              >* Đơn giá:</label
            >
            <input
              type="number"
              th:field="*{purchasePrice}"
              id="purchasePrice"
              min="0.00"
              step="0.01"
              required
              class="w-full border border-gray-300 rounded-md px-4 py-3 text-base focus:ring focus:ring-cafe-yellow focus:ring-opacity-50 outline-none transition-all duration-200"
              th:classappend="${#fields.hasErrors('purchasePrice')} ? 'border-red-500 bg-red-50' : 'border-gray-300'"
            />
            <p
              class="text-red-500 text-sm mt-1"
              th:if="${#fields.hasErrors('purchasePrice')}"
              th:errors="*{purchasePrice}"
            ></p>
          </div>

          <!-- Ngày mua -->
          <div>
            <label
              for="purchaseDate"
              class="block text-cafe-brown font-bold text-lg mb-2"
              >* Ngày mua:</label
            >
            <input
              type="date"
              th:field="*{purchaseDate}"
              id="purchaseDate"
              th:attr="max=${#dates.format(#dates.createNow(), 'yyyy-MM-dd')}"
              required
              class="w-full border border-gray-300 rounded-md px-4 py-3 text-base focus:ring focus:ring-cafe-yellow focus:ring-opacity-50 outline-none transition-all duration-200"
              th:classappend="${#fields.hasErrors('purchaseDate')} ? 'border-red-500 bg-red-50' : 'border-gray-300'"
            />
            <p
              class="text-red-500 text-sm mt-1"
              th:if="${#fields.hasErrors('purchaseDate')}"
              th:errors="*{purchaseDate}"
            ></p>
          </div>

          <!-- Nút hành động -->
          <div class="flex justify-between items-center mt-8">
            <a
              th:href="@{/equipment}"
              class="bg-gray-500 hover:bg-gray-600 text-white font-semibold px-6 py-2 rounded-md shadow transition-colors duration-200"
            >
              Hủy
            </a>
            <button
              type="submit"
              class="bg-cafe-yellow hover:bg-yellow-600 text-white font-semibold px-6 py-2 rounded-md shadow transition-colors duration-200"
            >
              Lưu
            </button>
          </div>
        </form>
      </div>
    </div>
  </body>
</html>
```

### **Edit Template (equipments/edit_equipment.html)**

```html
<!DOCTYPE html>
<html
  xmlns:th="http://www.thymeleaf.org"
  xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
  layout:decorate="~{layout}"
>
  <head>
    <title>Chỉnh sửa thiết bị</title>
  </head>
  <body>
    <div
      layout:fragment="content"
      class="bg-cafe-bg min-h-screen w-full flex justify-center items-start py-12 px-4"
    >
      <div
        class="w-full max-w-md bg-white p-10 rounded-2xl shadow-xl border border-gray-200"
      >
        <h1
          class="text-3xl font-bold text-cafe-brown mb-10 text-center tracking-wide"
        >
          Chỉnh sửa thiết bị
        </h1>

        <form
          th:action="@{'/equipment/' + ${equipmentId} + '/edit'}"
          th:object="${equipment}"
          method="post"
          class="space-y-6"
        >
          <!-- Tên thiết bị -->
          <div>
            <label
              for="equipmentName"
              class="block text-cafe-brown font-bold text-lg mb-2"
              >* Tên thiết bị:</label
            >
            <input
              type="text"
              th:field="*{equipmentName}"
              id="equipmentName"
              required
              minlength="5"
              pattern="^(?!\s*$).{5,}$"
              title="Tên thiết bị phải có ít nhất 5 ký tự và không được để trống hoặc toàn dấu cách"
              class="w-full border border-gray-300 rounded-md px-4 py-3 text-base focus:ring focus:ring-cafe-yellow focus:ring-opacity-50 outline-none transition-all duration-200"
              th:classappend="${#fields.hasErrors('equipmentName')} ? 'border-red-500 bg-red-50' : 'border-gray-300'"
            />
            <p
              class="text-red-500 text-sm mt-1"
              th:if="${#fields.hasErrors('equipmentName')}"
              th:errors="*{equipmentName}"
            ></p>
          </div>

          <!-- Số lượng -->
          <div>
            <label
              for="quantity"
              class="block text-cafe-brown font-bold text-lg mb-2"
              >* Số lượng:</label
            >
            <input
              type="number"
              th:field="*{quantity}"
              id="quantity"
              min="1"
              required
              class="w-full border border-gray-300 rounded-md px-4 py-3 text-base focus:ring focus:ring-cafe-yellow focus:ring-opacity-50 outline-none transition-all duration-200"
              th:classappend="${#fields.hasErrors('quantity')} ? 'border-red-500 bg-red-50' : 'border-gray-300'"
            />
            <p
              class="text-red-500 text-sm mt-1"
              th:if="${#fields.hasErrors('quantity')}"
              th:errors="*{quantity}"
            ></p>
          </div>

          <!-- Đơn giá -->
          <div>
            <label
              for="purchasePrice"
              class="block text-cafe-brown font-bold text-lg mb-2"
              >* Đơn giá:</label
            >
            <input
              type="number"
              th:field="*{purchasePrice}"
              id="purchasePrice"
              min="0.00"
              step="0.01"
              required
              class="w-full border border-gray-300 rounded-md px-4 py-3 text-base focus:ring focus:ring-cafe-yellow focus:ring-opacity-50 outline-none transition-all duration-200"
              th:classappend="${#fields.hasErrors('purchasePrice')} ? 'border-red-500 bg-red-50' : 'border-gray-300'"
            />
            <p
              class="text-red-500 text-sm mt-1"
              th:if="${#fields.hasErrors('purchasePrice')}"
              th:errors="*{purchasePrice}"
            ></p>
          </div>

          <!-- Ngày mua -->
          <div>
            <label
              for="purchaseDate"
              class="block text-cafe-brown font-bold text-lg mb-2"
              >* Ngày mua:</label
            >
            <input
              type="date"
              th:field="*{purchaseDate}"
              id="purchaseDate"
              th:attr="max=${#dates.format(#dates.createNow(), 'yyyy-MM-dd')}"
              required
              class="w-full border border-gray-300 rounded-md px-4 py-3 text-base focus:ring focus:ring-cafe-yellow focus:ring-opacity-50 outline-none transition-all duration-200"
              th:classappend="${#fields.hasErrors('purchaseDate')} ? 'border-red-500 bg-red-50' : 'border-gray-300'"
            />
            <p
              class="text-red-500 text-sm mt-1"
              th:if="${#fields.hasErrors('purchaseDate')}"
              th:errors="*{purchaseDate}"
            ></p>
          </div>

          <!-- Nút hành động -->
          <div class="flex justify-between items-center mt-8">
            <a
              th:href="@{/equipment}"
              class="bg-gray-500 hover:bg-gray-600 text-white font-semibold px-6 py-2 rounded-md shadow transition-colors duration-200"
            >
              Hủy
            </a>
            <button
              type="submit"
              class="bg-cafe-yellow hover:bg-yellow-600 text-white font-semibold px-6 py-2 rounded-md shadow transition-colors duration-200"
            >
              Cập nhật
            </button>
          </div>
        </form>
      </div>
    </div>
  </body>
</html>
```

## 📝 **Quy tắc đặt tên Templates**

### **Templates cần thiết cho mỗi entity:**

- **Layout**: `layout.html` (layout chính)
- **Entity templates**: `[entities]/[action]_[entity].html`
  - `equipments/create_equipment.html` - Form tạo mới
  - `equipments/edit_equipment.html` - Form chỉnh sửa
  - `equipments/list_equipment.html` - Danh sách với tìm kiếm

### **Features của List Template:**

- **Search form** với multiple criteria
- **Date range filtering**
- **Responsive table** với empty state
- **Pagination** với search parameters preserved
- **Delete confirmation modal**
- **Flash messages** auto-hide
- **Action buttons** với icons

### **Controller Methods cần hỗ trợ search:**

```java
@GetMapping
public String listEquipments(@RequestParam(required = false) String search,
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           Model model) {
    // Implementation với search logic
}
```

## 📋 **Development Guidelines**

### **Khi phát triển feature mới:**

1. **Tạo Entity** với JPA annotations và audit fields (@PrePersist, @PreUpdate)
2. **Tạo Repository** với JpaRepository và custom query methods nếu cần
3. **Tạo DTOs** (DTO, CreateRequest, UpdateRequest) với validation annotations
4. **Tạo MapStruct Mapper** với đầy đủ mappings và custom formatting methods
5. **Tạo Service** với business logic, validation và transaction management
6. **Tạo Controller** với MVC mappings, search parameters và pagination support
7. **Tạo Thymeleaf templates** với TailwindCSS:
   - `create_[entity].html` - Form tạo mới
   - `edit_[entity].html` - Form chỉnh sửa
   - `list_[entity].html` - Danh sách với search và pagination
8. **Cập nhật navigation** trong fragments/nav để thêm menu item
9. **Test toàn bộ CRUD operations** trên browser
10. **Kiểm tra responsive design** trên mobile/tablet

### **MapStruct Best Practices:**

- Sử dụng `componentModel = "spring"` để tích hợp với Spring
- Tạo custom mappings với `@Named` cho complex transformations (format price, date)
- Ignore fields không cần thiết với `@Mapping(target = "field", ignore = true)`
- Sử dụng `qualifiedByName` cho custom mapping methods
- Tạo separate methods cho create vs update mapping
- Test mapper với unit tests

### **TailwindCSS Styling Guidelines:**

- **Custom Colors**: Luôn sử dụng `cafe-bg`, `cafe-brown`, `cafe-yellow` thay vì hardcode colors
- **Form Styling**:
  - Input fields: `w-full border border-gray-300 rounded-md px-4 py-3 focus:ring focus:ring-cafe-yellow`
  - Error states: `border-red-500 bg-red-50`
  - Labels: `text-cafe-brown font-bold text-lg mb-2`
- **Button Patterns**:
  - Primary: `bg-cafe-yellow hover:bg-yellow-600 text-white font-semibold px-6 py-2 rounded-md shadow`
  - Secondary: `bg-gray-500 hover:bg-gray-600 text-white font-semibold px-6 py-2 rounded-md shadow`
  - Danger: `bg-red-500 hover:bg-red-600 text-white font-medium px-4 py-2 rounded`
- **Responsive Design**: Sử dụng `md:grid-cols-3`, `sm:px-6`, `lg:px-8` cho mobile-first approach
- **Transitions**: Luôn thêm `transition-colors duration-200` cho hover effects

### **Controller Patterns:**

- **Search Support**: Tất cả list endpoints phải hỗ trợ search và date filtering
- **Pagination**: Sử dụng `@RequestParam(defaultValue = "0") int page` và `size`
- **Flash Messages**: Sử dụng `RedirectAttributes.addFlashAttribute()` cho success/error messages
- **Form Validation**: Luôn check `BindingResult.hasErrors()` trước khi xử lý
- **Exception Handling**: Wrap business logic trong try-catch và return appropriate error messages

### **Service Layer Patterns:**

- **Transaction Management**: Sử dụng `@Transactional` cho write operations
- **Read-Only Operations**: Sử dụng `@Transactional(readOnly = true)` cho read operations
- **Business Validation**: Tạo private methods cho validation logic
- **Exception Handling**: Throw custom exceptions với meaningful messages

### **Template Structure Conventions:**

- **File Naming**: `[entities]/[action]_[entity].html` (ví dụ: `equipments/create_equipment.html`)
- **Layout Inheritance**: Luôn sử dụng `layout:decorate="~{layout}"`
- **Form Objects**: Sử dụng `th:object="${entity}"` và `th:field="*{property}"`
- **Error Display**: Hiển thị validation errors với `th:if="${#fields.hasErrors('field')}" th:errors="*{field}"`
- **Search Forms**: Preserve search parameters trong pagination links
- **Modal Confirmations**: Sử dụng cho delete operations với proper accessibility

### **Database & JPA Patterns:**

- **Naming Conventions**:
  - Table names: plural (equipments, customers)
  - Column names: snake_case (equipment_name, purchase_date)
- **Audit Fields**: Luôn có `created_date` và `updated_date`
- **Validation**: Combine JPA constraints với Bean Validation annotations
- **Queries**: Prefer method name queries over @Query when possible

### **Error Handling Best Practices:**

- **User-Friendly Messages**: Tất cả error messages phải bằng tiếng Việt và dễ hiểu
- **Validation Messages**: Customize validation messages trong annotations
- **Global Exception Handler**: Tạo @ControllerAdvice để handle common exceptions
- **Logging**: Log errors với appropriate levels (ERROR, WARN, INFO)

### **Performance Considerations:**

- **Lazy Loading**: Sử dụng FetchType.LAZY cho collections
- **N+1 Queries**: Tránh bằng cách sử dụng JOIN FETCH hoặc @EntityGraph
- **Pagination**: Luôn implement pagination cho large datasets
- **Caching**: Consider caching cho reference data

### **Security Guidelines:**

- **Input Validation**: Validate tất cả user input với Bean Validation
- **XSS Prevention**: Thymeleaf tự động escape, nhưng cẩn thận với th:utext
- **CSRF Protection**: Spring Security tự động enable, đảm bảo forms có CSRF token
- **SQL Injection**: Sử dụng parameterized queries, tránh string concatenation

### **Testing Strategy:**

- **Unit Tests**: Test service layer business logic
- **Integration Tests**: Test controller với @SpringBootTest
- **Repository Tests**: Test custom queries với @DataJpaTest
- **Template Tests**: Manual testing cho UI/UX

---

**Lưu ý quan trọng**:

- Dự án **KHÔNG** sử dụng detail templates - chỉ có list, create, edit
- Luôn implement search functionality cho list pages
- Maintain consistency trong naming conventions và styling patterns
- Focus vào user experience với proper error handling và responsive design
