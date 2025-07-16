package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.request.promotion.CreatePromotionRequest;
import com.viettridao.cafe.dto.request.promotion.UpdatePromotionRequest;
import com.viettridao.cafe.dto.response.promotion.PromotionResponsePage;
import com.viettridao.cafe.model.PromotionEntity;
import com.viettridao.cafe.repository.PromotionRepository;
import com.viettridao.cafe.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;


    @Override
    public List<PromotionEntity> getAllPromotion() {
        return promotionRepository.getAllPromotion();
    }


    @Override
    public void deletePromotion(Integer id) {
        PromotionEntity promotionEntity = getPromotionById(id);
        promotionEntity.setIsDeleted(true);
        promotionRepository.save(promotionEntity);
    }

    @Override
    public PromotionEntity getPromotionById(Integer id) {
        return promotionRepository.findById(id).orElseThrow(()-> new RuntimeException("Không tìm thấy khuyến mãi có ID =" + id));
    }

    @Override
    public void createPromotion(CreatePromotionRequest request) {
        PromotionEntity promotionEntity = new PromotionEntity();
        promotionEntity.setPromotionName(request.getPromotionName());
        promotionEntity.setStartDate(request.getStartDate());
        promotionEntity.setEndDate(request.getEndDate());
        promotionEntity.setDiscountValue(request.getDiscountValue());
        promotionEntity.setIsDeleted(false);

        promotionRepository.save(promotionEntity);
    }

    @Override
    public void updatePromotion(UpdatePromotionRequest request) {
        PromotionEntity promotionEntity = getPromotionById(request.getId());

        promotionEntity.setPromotionName(request.getPromotionName());
        promotionEntity.setStartDate(request.getStartDate());
        promotionEntity.setEndDate(request.getEndDate());
        promotionEntity.setDiscountValue(request.getDiscountValue());

        promotionRepository.save(promotionEntity);

    }

    @Override
    public PromotionResponsePage getAllPromotionPage(String namePromotion, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PromotionEntity> promotionEntities;
        if(StringUtils.hasText(namePromotion)){
            promotionEntities = promotionRepository.getAllPromotionPageSearch(namePromotion,pageable);
        }
        else{
            promotionEntities = promotionRepository.getAllPromotionPage(pageable);
        }

        PromotionResponsePage promotionResponsePage = new PromotionResponsePage();
        promotionResponsePage.setPageSize(promotionEntities.getSize());
        promotionResponsePage.setTotalPages(promotionEntities.getTotalPages());
        promotionResponsePage.setPageNumber(promotionEntities.getNumber());
        promotionResponsePage.setPromotionPage(promotionEntities.getContent());
        promotionResponsePage.setTotalElements(promotionEntities.getTotalElements());

        return promotionResponsePage;
    }
}
