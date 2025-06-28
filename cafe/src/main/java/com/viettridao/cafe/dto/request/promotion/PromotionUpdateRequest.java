package com.viettridao.cafe.dto.request.promotion;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PromotionUpdateRequest {

    private Integer id;

    private String promotionName;

    private LocalDate startDate;

    private LocalDate endDate;

    private Double discountValue;

    private Boolean status;

    private String description;


}
