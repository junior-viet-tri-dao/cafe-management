package com.viettridao.cafe.service.promotion;

import java.util.List;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.viettridao.cafe.dto.request.promotion.PromotionCreateRequest;
import com.viettridao.cafe.dto.request.promotion.PromotionUpdateRequest;
import com.viettridao.cafe.dto.response.promotion.PromotionResponse;
import com.viettridao.cafe.mapper.PromotionMapper;
import com.viettridao.cafe.model.PromotionEntity;
import com.viettridao.cafe.repository.PromotionRepository;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements IPromotionService{

    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;

    @Override
    public List<PromotionResponse> getPromotionAll() {
        return promotionRepository.findAllByDeletedFalse()
                .stream()
                .map(promotionMapper::toResponse)
                .toList();
    }

    @Override
    public PromotionResponse getByPromotionById(Integer id) {

        PromotionEntity entity = findPromotionOrThrow(id);

        return promotionMapper.toResponse(entity);
    }

    @Override
    @Transactional
    public void createPromotion(PromotionCreateRequest request) {

        PromotionEntity entity = promotionMapper.toEntity(request);

        promotionRepository.save(entity);

    }

    @Override
    public PromotionUpdateRequest getUpdateForm(Integer id) {

        return promotionMapper.toUpdateRequest(findPromotionOrThrow(id));

    }

    @Override
    @Transactional
    public void updatePromotion(Integer id, PromotionUpdateRequest request) {

        PromotionEntity existing = findPromotionOrThrow(id);

        promotionMapper.updateEntityFromRequest(request,existing);

        promotionRepository.save(existing);

    }

    @Override
    @Transactional
    public void deletePromotion(Integer id) {

        PromotionEntity entity = findPromotionOrThrow(id);

        entity.setDeleted(true);

        promotionRepository.save(entity);

    }

    // Kiếm khuyến mãi theo id
    public PromotionEntity findPromotionOrThrow(Integer id) {
        return promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khuyến mãi với id = " + id));
    }
}
