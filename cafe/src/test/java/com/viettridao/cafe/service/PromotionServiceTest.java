package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.promotion.CreatePromotionRequest;
import com.viettridao.cafe.dto.request.promotion.UpdatePromotionRequest;
import com.viettridao.cafe.model.PromotionEntity;
import com.viettridao.cafe.repository.PromotionRepository;
import com.viettridao.cafe.service.impl.PromotionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
class PromotionServiceTest {
    @MockBean
    private PromotionRepository promotionRepository;

    @Autowired
    private PromotionServiceImpl promotionService;

    @Test
    @DisplayName("Tạo khuyến mãi thành công")
    void testCreatePromotion_Success() {
        CreatePromotionRequest request = new CreatePromotionRequest();
        request.setPromotionName("Giảm giá hè");
        request.setDiscountValue(10.0);
        request.setStartDate(java.time.LocalDate.now());
        request.setEndDate(java.time.LocalDate.now().plusDays(7));
        PromotionEntity entity = new PromotionEntity();
        entity.setId(1);
        when(promotionRepository.save(any(PromotionEntity.class))).thenReturn(entity);
        assertThatCode(() -> promotionService.createPromotion(request)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Cập nhật khuyến mãi không tồn tại")
    void testUpdatePromotion_NotFound() {
        UpdatePromotionRequest request = new UpdatePromotionRequest();
        request.setId(99);
        when(promotionRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> promotionService.updatePromotion(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Không tìm thấy khuyến mãi");
    }

    @Test
    @DisplayName("Xóa khuyến mãi không tồn tại")
    void testDeletePromotion_NotFound() {
        when(promotionRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> promotionService.deletePromotion(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Không tìm thấy khuyến mãi");
    }
}
