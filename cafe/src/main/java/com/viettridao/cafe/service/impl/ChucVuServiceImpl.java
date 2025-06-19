package com.viettridao.cafe.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.viettridao.cafe.model.ChucVu;
import com.viettridao.cafe.repository.ChucVuRepository;
import com.viettridao.cafe.service.ChucVuService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChucVuServiceImpl implements ChucVuService {

    private final ChucVuRepository chucVuRepository;

    @Override
    public List<ChucVu> getListChucVu() {
        return chucVuRepository.findAll();
    }

}
