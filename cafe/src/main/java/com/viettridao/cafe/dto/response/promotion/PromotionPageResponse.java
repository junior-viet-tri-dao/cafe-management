package com.viettridao.cafe.dto.response.promotion;

import com.viettridao.cafe.dto.response.PageResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PromotionPageResponse extends PageResponse {
    private List<PromotionResponse> promotions;
}
