package com.viettridao.cafe.service.promotion;

import java.util.List;

import com.viettridao.cafe.dto.request.promotion.PromotionCreateRequest;
import com.viettridao.cafe.dto.request.promotion.PromotionUpdateRequest;
import com.viettridao.cafe.dto.response.promotion.PromotionResponse;

public interface IPromotionService {

    List<PromotionResponse> getPromotionAll();

    PromotionResponse getByPromotionById(Integer id);

    void createPromotion(PromotionCreateRequest request);

    PromotionUpdateRequest getUpdateForm(Integer id);

    void updatePromotion(Integer id, PromotionUpdateRequest request);

    void deletePromotion(Integer id);

}
