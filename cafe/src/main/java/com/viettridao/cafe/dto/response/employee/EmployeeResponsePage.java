package com.viettridao.cafe.dto.response.employee;

import com.viettridao.cafe.dto.response.ImportResponse;
import com.viettridao.cafe.dto.response.ResponsePage;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class EmployeeResponsePage extends ResponsePage {

    private List<EmployeeResponse> employees;
}
