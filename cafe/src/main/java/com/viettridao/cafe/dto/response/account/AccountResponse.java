package com.viettridao.cafe.dto.response.account;

import java.util.List;

import lombok.*;

import com.viettridao.cafe.dto.response.employee.EmployeeResponse;
import com.viettridao.cafe.dto.response.expense.ExpenseResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {

    private Integer id;

    private String fullName;

    private String phoneNumber;

    private String address;

    private String imageUrl;

    private Integer positionId;

    private String positionName;

    private Double salary;

    private String username;

    private String password;

    private List<ExpenseResponse> expenses;
}
