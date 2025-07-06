package com.viettridao.cafe.dto.request.account;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRequest {
    private PositionRequest position;
    private String fullName;
    private String address;
    private String phoneNumber;

    public EmployeeRequest() {}


}
