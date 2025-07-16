package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.request.product.CreateProductRequest;
import com.viettridao.cafe.dto.request.product.UpdateProductRequest;
import com.viettridao.cafe.dto.response.product.ProductResponsePage;
import com.viettridao.cafe.model.ProductEntity;
import com.viettridao.cafe.model.UnitEntity;
import com.viettridao.cafe.repository.ProductRepository;
import com.viettridao.cafe.repository.UnitRepository;
import com.viettridao.cafe.service.ProductService;
import com.viettridao.cafe.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService {
    private final UnitRepository unitRepository;

    @Override
    public List<UnitEntity> getAllUnit() {
        return unitRepository.findAll();
    }
}
