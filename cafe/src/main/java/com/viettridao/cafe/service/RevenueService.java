package com.viettridao.cafe.service;


import com.viettridao.cafe.dto.request.account.UpdateAccountRequest;
import com.viettridao.cafe.dto.request.reservation.RevenueFilterRequest;
import com.viettridao.cafe.dto.response.revenue.RevenueResponse;
import com.viettridao.cafe.model.AccountEntity;

public interface RevenueService {


    RevenueResponse getRevenueSummary(RevenueFilterRequest filter);
}
