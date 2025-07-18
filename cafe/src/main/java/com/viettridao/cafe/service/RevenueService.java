package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.reservation.RevenueFilterRequest;
import com.viettridao.cafe.dto.response.revenue.RevenueResponse;

/**
 * RevenueService
 *
 * Version 1.0
 *
 * Date: 18-07-2025
 *
 * Copyright
 *
 * Modification Logs:
 * DATE         AUTHOR      DESCRIPTION
 * -------------------------------------------------------
 * 18-07-2025   mirodoan    Create
 */
public interface RevenueService {
    /**
     * Lấy tổng hợp doanh thu theo bộ lọc.
     *
     * @param request Bộ lọc thống kê doanh thu.
     * @return Đối tượng RevenueResponse chứa thông tin tổng hợp doanh thu.
     */
    RevenueResponse getRevenueSummary(RevenueFilterRequest request);
}