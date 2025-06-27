package com.viettridao.cafe.service.impl;

import com.viettridao.cafe.dto.response.warehouse.WareHousePageResponse;
import com.viettridao.cafe.dto.response.warehouse.WareHouseResponse;
import com.viettridao.cafe.repository.ExportRepository;
import com.viettridao.cafe.repository.ImportRepository;
import com.viettridao.cafe.service.WareHouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WareHouseServiceImpl implements WareHouseService {
    private final ImportRepository importRepository;
    private final ExportRepository exportRepository;

    @Override
    public WareHousePageResponse getAllWareHouses(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WareHouseResponse> wareHouseResponses = importRepository.getAllWarehouses(keyword, pageable);

        WareHousePageResponse response = new WareHousePageResponse();
        response.setPageNumber(wareHouseResponses.getNumber());
        response.setPageSize(wareHouseResponses.getSize());
        response.setTotalElements(wareHouseResponses.getTotalElements());
        response.setTotalPages(wareHouseResponses.getTotalPages());
        response.setWarehouses(wareHouseResponses.getContent());

        return response;
    }
}
