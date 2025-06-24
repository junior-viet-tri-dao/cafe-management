package com.viettridao.cafe.dto.request.employee;

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
public class AddEmployeeRequest {
    private String fullName;
    private String phoneNumber;
    private String address;
    private Integer positionId;
    private String username;
    private String password;
}
