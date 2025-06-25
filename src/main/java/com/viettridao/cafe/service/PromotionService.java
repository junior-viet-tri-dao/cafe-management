package com.viettridao.cafe.service;

import java.util.List;

import com.viettridao.cafe.dto.request.promotion.UpdatePromotionRequest;
import com.viettridao.cafe.dto.response.promotion.PromotionResponse;
import com.viettridao.cafe.model.PromotionEntity;

public interface PromotionService {
    /**
     * Retrieves all promotions.
     *
     * @return a list of all promotions
     */
    List<PromotionResponse> getAllPromotions();

    /**
     * Retrieves a promotion by its ID.
     *
     * @param id the ID of the promotion
     * @return the promotion with the specified ID, or null if not found
     */
    PromotionResponse getPromotionById(Integer id);

    /**
     * Creates a new promotion.
     *
     * @param promotion the promotion to create
     * @return the created promotion
     */
    PromotionEntity createPromotion(PromotionEntity promotion);

    /**
     * Updates an existing promotion.
     *
     * @param id        the ID of the promotion to update
     * @param promotion the updated promotion data
     * @return the updated promotion
     */
    PromotionEntity updatePromotion(UpdatePromotionRequest request);

    /**
     * Deletes a promotion by its ID.
     *
     * @param id the ID of the promotion to delete
     */
    void deletePromotion(Integer id);
}
