package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.reservation.RevenueFilterRequest;
import com.viettridao.cafe.dto.response.revenue.RevenueResponse;

public interface RevenueService {
    RevenueResponse getRevenueSummary(RevenueFilterRequest request);
}