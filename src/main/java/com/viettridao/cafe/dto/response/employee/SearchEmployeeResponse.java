package com.viettridao.cafe.dto.response.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchEmployeeResponse {
    private Integer employeeId;
    private String fullName;
    private String positionName;
    private Integer salary;
}
