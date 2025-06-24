package com.viettridao.cafe.controller.response;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

/**
 * Lớp DTO (Data Transfer Object) dùng để trả về thông tin thiết bị (Equipment)
 * dưới dạng phản hồi cho client.
 */
@Getter
@Setter
public class EquipmentResponse {
    private Integer id;

    private String equipmentName;

    private LocalDate purchaseDate;

    private Integer quantity;

    private Double purchasePrice;

    private Double totalPrice;

    private String notes;
}