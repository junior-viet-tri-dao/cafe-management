package com.viettridao.cafe.dto.response.revenue;

import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.dto.response.reservation.MenuItemReservationResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class RevenueResponse {

    private List<RevenueItemResponse> summaries;
    private Double totalIncome;
    private Double totalExpense;
}
