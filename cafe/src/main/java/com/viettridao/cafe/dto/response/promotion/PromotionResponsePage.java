package com.viettridao.cafe.dto.response.promotion;

import com.viettridao.cafe.dto.response.PageResponse;
import com.viettridao.cafe.model.PromotionEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PromotionResponsePage extends PageResponse {
    private List<PromotionEntity> promotionPage;
}
