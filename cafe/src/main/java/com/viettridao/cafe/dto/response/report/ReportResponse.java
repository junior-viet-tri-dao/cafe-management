package com.viettridao.cafe.dto.response.report;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportResponse {

    private List<ItemReportResponse> items;

    private Double totalRevenue;

    private Double totalExpense;
}
