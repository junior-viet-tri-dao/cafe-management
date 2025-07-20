package com.viettridao.cafe.service.revenue;

import com.viettridao.cafe.dto.request.revenue.RevenueFilterRequest;
import com.viettridao.cafe.dto.response.revenue.RevenueResponse;

public interface IRevenueService {

    RevenueResponse getRevenueSummary(RevenueFilterRequest request);

}