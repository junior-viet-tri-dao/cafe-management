package com.viettridao.cafe.dto.response.promotion;

import lombok.Getter;
import lombok.Setter;

import java.time.*;

@Getter
@Setter
public class PromotionResponse {

    private Integer id;

    private String promotionName;

    private LocalDate startDate;

    private LocalDate endDate;

    private Double discountValue;

    private Boolean status;

    private String description;

    private Boolean deleted;
}
