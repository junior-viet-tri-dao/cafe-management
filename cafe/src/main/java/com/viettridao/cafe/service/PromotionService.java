package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.promotion.CreatePromotionRequest;
import com.viettridao.cafe.dto.request.promotion.UpdatePromotionRequest;
import com.viettridao.cafe.dto.response.promotion.PromotionPageResponse;
import com.viettridao.cafe.model.PromotionEntity;
import org.springframework.data.domain.Pageable;

/**
 * PromotionService
 *
 * Version 1.0
 *
 * Date: 18-07-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE         AUTHOR      DESCRIPTION
 * -------------------------------------------------------
 * 18-07-2025   mirodoan    Create
 */
public interface PromotionService {

    /**
     * Lấy danh sách các khuyến mãi còn hiệu lực.
     *
     * @param pageable Đối tượng phân trang.
     * @return Đối tượng PromotionPageResponse chứa danh sách khuyến mãi còn hiệu lực và thông tin phân trang.
     */
    PromotionPageResponse getValidPromotions(Pageable pageable);

    /**
     * Lấy danh sách tất cả các khuyến mãi.
     *
     * @param pageable Đối tượng phân trang.
     * @return Đối tượng PromotionPageResponse chứa danh sách tất cả khuyến mãi và thông tin phân trang.
     */
    PromotionPageResponse getAllPromotions(Pageable pageable);

    /**
     * Lấy thông tin chi tiết của một khuyến mãi dựa trên ID.
     *
     * @param id ID của khuyến mãi cần lấy thông tin.
     * @return Thực thể PromotionEntity tương ứng với ID.
     */
    PromotionEntity getPromotionById(Integer id);

    /**
     * Tạo mới một khuyến mãi.
     *
     * @param request Đối tượng chứa thông tin cần thiết để tạo khuyến mãi mới.
     * @return Thực thể PromotionEntity vừa được tạo.
     */
    PromotionEntity createPromotion(CreatePromotionRequest request);

    /**
     * Cập nhật thông tin khuyến mãi.
     *
     * @param request Đối tượng chứa thông tin cần cập nhật cho khuyến mãi.
     */
    void updatePromotion(UpdatePromotionRequest request);

    /**
     * Xóa mềm một khuyến mãi (đặt isDeleted = true).
     *
     * @param id ID của khuyến mãi cần xóa.
     */
    void deletePromotion(Integer id);
}