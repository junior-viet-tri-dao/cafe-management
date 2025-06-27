package com.viettridao.cafe.service.impl;

import java.util.List;
import java.util.Optional;

import com.viettridao.cafe.dto.request.promotion.AddPromotionRequest;
import com.viettridao.cafe.dto.request.promotion.UpdatePromotionRequest;
import com.viettridao.cafe.dto.response.promotion.PromotionResponse;
import com.viettridao.cafe.mapper.PromotionMapper;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
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
        // Validate ID
        if (id == null || id <= 0) {
            return null;
        }

        try {
            // Tìm promotion theo ID
            Optional<PromotionEntity> promotionOpt = promotionRepository.findById(id);

            if (promotionOpt.isPresent()) {
                PromotionEntity promotion = promotionOpt.get();

                // Kiểm tra promotion có bị xóa không (nếu có soft delete)
                if (promotion.getIsDeleted() != null && promotion.getIsDeleted()) {
                    return null; // Coi như không tìm thấy
                }

                // Convert to DTO
                return promotionMapper.convertToDto(promotion);
            } else {
                // Không tìm thấy promotion
                return null;
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi lấy promotion với ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public PromotionEntity createPromotion(AddPromotionRequest request) {
        // 1. Validate input
        if (request == null) {
            throw new IllegalArgumentException("Dữ liệu khuyến mãi không được để trống");
        }

        // Thêm validation sau step 1:
        // 1.1. Validate required fields (phòng trường hợp annotation validation fail)
        if (request.getPromotionName() == null || request.getPromotionName().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên khuyến mãi không được để trống");
        }

        if (request.getStartDate() == null) {
            throw new IllegalArgumentException("Ngày bắt đầu không được để trống");
        }

        if (request.getEndDate() == null) {
            throw new IllegalArgumentException("Ngày kết thúc không được để trống");
        }

        if (request.getDiscountValue() == null || request.getDiscountValue() <= 0 || request.getDiscountValue() > 100) {
            throw new IllegalArgumentException("Giá trị giảm giá phải từ 0.1% đến 100%");
        }

        // 2. Validate business logic - ngày kết thúc phải sau ngày bắt đầu
        if (request.getEndDate() != null && request.getStartDate() != null) {
            if (request.getEndDate().isBefore(request.getStartDate()) ||
                    request.getEndDate().isEqual(request.getStartDate())) {
                throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu");
            }
        }

        // 3. Kiểm tra tên khuyến mãi trùng lặp
        String promotionName = request.getPromotionName().trim();
        boolean nameExists = promotionRepository.findAll().stream()
                .anyMatch(p -> p.getPromotionName().equalsIgnoreCase(promotionName) &&
                        (p.getIsDeleted() == null || !p.getIsDeleted()));

        if (nameExists) {
            throw new IllegalArgumentException("Tên khuyến mãi '" + promotionName + "' đã tồn tại");
        }

        // 4. Tạo entity mới
        PromotionEntity promotion = new PromotionEntity();
        promotion.setPromotionName(promotionName);
        promotion.setStartDate(request.getStartDate());
        promotion.setEndDate(request.getEndDate());
        promotion.setDiscountValue(request.getDiscountValue());
        // promotion.setStatus(request.getStatus() != null ? request.getStatus() :
        // true);

        // // Set description (có thể null)
        // if (request.getDescription() != null) {
        // String desc = request.getDescription().trim();
        // if (!desc.isEmpty()) {
        // if (desc.length() > 500) {
        // throw new IllegalArgumentException("Mô tả không được vượt quá 500 ký tự");
        // }
        // promotion.setDescription(desc);
        // }
        // }

        // Set default values cho soft delete
        promotion.setIsDeleted(false);

        // 5. Lưu vào database
        try {
            PromotionEntity saved = promotionRepository.save(promotion);
            System.out.println("Đã tạo promotion mới ID: " + saved.getId() + " - " + saved.getPromotionName());
            return saved;

        } catch (DataIntegrityViolationException e) {
            // Handle database constraint violations
            throw new RuntimeException("Dữ liệu vi phạm ràng buộc database: " + e.getMessage(), e);
        } catch (DataAccessException e) {
            // Handle database access issues
            throw new RuntimeException("Lỗi truy cập database khi tạo khuyến mãi: " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Lỗi khi tạo promotion: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Lỗi hệ thống khi tạo khuyến mãi: " + e.getMessage(), e);
        }
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
        // existingPromotion.setStatus(request.getStatus());

        // // Cập nhật description
        // if (request.getDescription() != null &&
        // !request.getDescription().trim().isEmpty()) {
        // existingPromotion.setDescription(request.getDescription().trim());
        // } else {
        // existingPromotion.setDescription(null);
        // }

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
