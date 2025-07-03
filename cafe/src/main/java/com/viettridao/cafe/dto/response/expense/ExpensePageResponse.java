package com.viettridao.cafe.dto.response.expense;

import com.viettridao.cafe.dto.response.PageResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExpensePageResponse extends PageResponse {
    private List<ExpenseResponse> expenses;
}
