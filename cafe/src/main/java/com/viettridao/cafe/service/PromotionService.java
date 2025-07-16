package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.promotion.CreatePromotionRequest;
import com.viettridao.cafe.dto.request.promotion.UpdatePromotionRequest;
import com.viettridao.cafe.dto.response.promotion.PromotionResponsePage;
import com.viettridao.cafe.model.PromotionEntity;

import java.util.List;

public interface PromotionService {
    List<PromotionEntity> getAllPromotion();

    void deletePromotion(Integer id);

    PromotionEntity getPromotionById(Integer id);

    void createPromotion(CreatePromotionRequest request);

    void updatePromotion(UpdatePromotionRequest request);

    PromotionResponsePage getAllPromotionPage(String namePromotion, int page, int size);
}
