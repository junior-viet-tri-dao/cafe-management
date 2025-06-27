package com.viettridao.cafe.service;


import com.viettridao.cafe.dto.request.promotion.CreatePromotionRequest;
import com.viettridao.cafe.dto.request.promotion.UpdatePromotionRequest;
import com.viettridao.cafe.dto.response.promotion.PromotionPageResponse;
import com.viettridao.cafe.model.PromotionEntity;

public interface PromotionService {
    PromotionPageResponse getAllPromotions(int page, int size);
    PromotionEntity createPromotion(CreatePromotionRequest request);
    void updatePromotion(UpdatePromotionRequest request);
    PromotionEntity getPromotionById(Integer id);
    void deletePromotion(Integer id);
}
