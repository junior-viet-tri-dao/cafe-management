package com.viettridao.cafe.dto.request.account;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PositionRequest {
    private String positionName;
    private Double salary;


    public PositionRequest() {}


}
