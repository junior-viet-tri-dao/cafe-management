package com.viettridao.cafe.mapper;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.viettridao.cafe.dto.response.reportstatistics.ReportItemResponse;
import com.viettridao.cafe.mapper.base.BaseMapper;

@Component
public class ReportMapper extends BaseMapper<Object, Object, ReportItemResponse> {

	public ReportMapper(ModelMapper modelMapper) {
		super(modelMapper, Object.class, Object.class, ReportItemResponse.class);
	}

	public ReportItemResponse toReportItem(LocalDate date, Double revenue, Double expense) {
		return new ReportItemResponse(date, revenue != null ? revenue : 0.0, expense != null ? expense : 0.0);
	}
}
