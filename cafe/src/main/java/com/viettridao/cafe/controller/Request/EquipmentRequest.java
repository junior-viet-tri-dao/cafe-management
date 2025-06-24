package com.viettridao.cafe.controller.Request;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Lớp DTO (Data Transfer Object) dùng để nhận dữ liệu yêu cầu từ client
 * liên quan đến thông tin thiết bị (Equipment). Bao gồm các ràng buộc kiểm tra dữ liệu.
 */
@Getter
@Setter
public class EquipmentRequest {
    // ID của thiết bị, không bắt buộc khi tạo mới
    private Integer id;

    /**
     * Tên thiết bị, không được để trống và tối đa 50 ký tự
     */
    @NotBlank(message = "Tên thiết bị không được để trống")
    @Size(max = 50, message = "Tên thiết bị không được vượt quá 50 ký tự")
    private String equipmentName;

    /**
     * Ngày mua thiết bị, không được để trống
     */
    @NotNull(message = "Ngày mua không được để trống")
    private LocalDate purchaseDate;

    /**
     * Số lượng thiết bị, phải lớn hơn 0 và không được để trống
     */
    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private Integer quantity;

    /**
     * Đơn giá thiết bị, phải lớn hơn 0 và không được để trống
     */
    @NotNull(message = "Đơn giá không được để trống")
    @Min(value = 9, message = "Đơn giá phải lớn hơn 0")
    private Double purchasePrice;

    /**
     * Ghi chú về thiết bị, không bắt buộc
     */
    private String notes;
}