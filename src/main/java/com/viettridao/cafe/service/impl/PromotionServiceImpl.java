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
        // 1. Validate ID
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID khuyến mãi không hợp lệ");
        }

        // 2. Kiểm tra promotion có tồn tại không
        PromotionEntity promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khuyến mãi với ID: " + id));

        // 3. Kiểm tra promotion đã bị xóa chưa (nếu có soft delete)
        if (promotion.getIsDeleted() != null && promotion.getIsDeleted()) {
            throw new RuntimeException("Khuyến mãi đã bị xóa trước đó");
        }

        // 4. Kiểm tra promotion có đang active không (business rule)
        if (promotion.getStatus() != null && promotion.getStatus()) {
            throw new RuntimeException("Không thể xóa khuyến mãi đang hoạt động. Vui lòng vô hiệu hóa trước khi xóa");
        }

        // 5. Kiểm tra promotion có đang được sử dụng không (nếu có liên kết với orders)
        // boolean isInUse = orderRepository.existsByPromotionId(id);
        // if (isInUse) {
        // throw new RuntimeException("Không thể xóa khuyến mãi đang được sử dụng trong
        // đơn hàng");
        // }

        try {
            // Chọn 1 trong 2 cách:

            // Cách 1: Soft Delete (Khuyến nghị)
            promotion.setIsDeleted(true);
            // promotion.setDeletedAt(LocalDateTime.now()); // Nếu có field này
            promotionRepository.save(promotion);

            // Cách 2: Hard Delete (Chỉ khi chắc chắn không cần lưu lại)
            // promotionRepository.delete(promotion);

            System.out.println("Đã xóa promotion ID: " + id + " - " + promotion.getPromotionName());

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa khuyến mãi: " + e.getMessage(), e);
        }
    }

}
