package com.viettridao.cafe.dto.request.account;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAccountRequest {
    private EmployeeRequest employee;
    private Integer id;

}
