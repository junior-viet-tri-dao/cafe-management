package com.viettridao.cafe.mapper;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.response.reportstatistics.ReportItemResponse;

@Component
public class ReportMapper {

	public ReportItemResponse toReportItem(LocalDate date, Double revenue, Double expense) {
		return new ReportItemResponse(date, revenue != null ? revenue : 0.0, expense != null ? expense : 0.0);
	}
}
