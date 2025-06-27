package com.viettridao.cafe.dto.response.employee;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.viettridao.cafe.dto.response.account.AccountResponse;
import com.viettridao.cafe.dto.response.export.ExportResponse;
import com.viettridao.cafe.dto.response.imports.ImportResponse;
import com.viettridao.cafe.dto.response.position.PositionResponse;
import com.viettridao.cafe.dto.response.reservation.ReservationResponse;

@Getter
@Setter
public class EmployeeResponse {

    private Integer id;

    private String fullName;

    private String phoneNumber;

    private String address;

    private Boolean deleted;

    private PositionResponse position;

    private List<ImportResponse> imports;

    private List<ExportResponse> exports;

    private List<ReservationResponse> reservations;

}
