package com.viettridao.cafe.service.impl;

import java.util.List;

import com.viettridao.cafe.dto.request.promotion.UpdatePromotionRequest;
import com.viettridao.cafe.dto.response.promotion.PromotionResponse;
import com.viettridao.cafe.mapper.PromotionMapper;
import org.springframework.stereotype.Service;

import com.viettridao.cafe.model.PromotionEntity;
import com.viettridao.cafe.repository.PromotionRepository;
import com.viettridao.cafe.service.PromotionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;

    @Override
    public List<PromotionResponse> getAllPromotions() {
        return promotionRepository.findAll().stream()
                .map(promotion -> promotionMapper.convertToDto(promotion))
                .toList();
    }

    @Override
    public PromotionResponse getPromotionById(Integer id) {
        PromotionEntity promotion = promotionRepository.findById(id).orElse(null);
        return promotionMapper.convertToDto(promotion);
    }

    @Override
    public PromotionEntity createPromotion(PromotionEntity promotion) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createPromotion'");
    }

    @Override
    public PromotionEntity updatePromotion(UpdatePromotionRequest request) {
        // Validate input
        if (request == null) {
            throw new IllegalArgumentException("Dữ liệu cập nhật không được để trống");
        }

        if (request.getId() == null || request.getId() <= 0) {
            throw new IllegalArgumentException("ID khuyến mãi không hợp lệ");
        }

        // Tìm promotion theo ID
        PromotionEntity existingPromotion = promotionRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khuyến mãi với ID: " + request.getId()));

        // Kiểm tra ngày hợp lệ
        if (request.getEndDate().isBefore(request.getStartDate()) ||
                request.getEndDate().isEqual(request.getStartDate())) {
            throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu");
        }

        // Cập nhật thông tin
        existingPromotion.setPromotionName(request.getPromotionName().trim());
        existingPromotion.setStartDate(request.getStartDate());
        existingPromotion.setEndDate(request.getEndDate());
        existingPromotion.setDiscountValue(request.getDiscountValue());
        existingPromotion.setStatus(request.getStatus());

        // Cập nhật description
        if (request.getDescription() != null && !request.getDescription().trim().isEmpty()) {
            existingPromotion.setDescription(request.getDescription().trim());
        } else {
            existingPromotion.setDescription(null);
        }

        try {
            PromotionEntity saved = promotionRepository.save(existingPromotion);
            System.out.println("Đã cập nhật promotion ID: " + saved.getId() + " - " + saved.getPromotionName());
            return saved;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lưu khuyến mãi: " + e.getMessage(), e);
        }
    }

    @Override
    public void deletePromotion(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deletePromotion'");
    }

}
