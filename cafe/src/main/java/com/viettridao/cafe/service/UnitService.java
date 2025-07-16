package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.promotion.CreatePromotionRequest;
import com.viettridao.cafe.dto.request.promotion.UpdatePromotionRequest;
import com.viettridao.cafe.dto.response.promotion.PromotionResponsePage;
import com.viettridao.cafe.model.PromotionEntity;
import com.viettridao.cafe.model.UnitEntity;

import java.util.List;

public interface UnitService {

    List<UnitEntity> getAllUnit();
}
