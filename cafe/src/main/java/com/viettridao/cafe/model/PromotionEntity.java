package com.viettridao.cafe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "promotions")//khuyenmai
public class PromotionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotion_id")
    private Integer id;

    @NotBlank(message = "Tên khuyến mãi không được để trống")
    @Size(max = 100, message = "Tên khuyến mãi không được vượt quá 100 ký tự")
    @Column(name = "promotion_name")
    private String promotionName;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    @FutureOrPresent(message = "Ngày bắt đầu phải là ngày hiện tại hoặc trong tương lai")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "start_date")
    private LocalDate startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "end_date")
    private LocalDate endDate;

    @NotNull(message = "Giá trị giảm giá không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Giá trị giảm giá không thể là số âm")
    @Column(name = "discount_value")
    private Double discountValue;

    @NotNull(message = "Trạng thái không được để trống")
    @Column(name = "status")
    private Boolean status;

    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    @Column(name = "description")
    private String description;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL)
    private List<InvoiceEntity> invoices;
}
