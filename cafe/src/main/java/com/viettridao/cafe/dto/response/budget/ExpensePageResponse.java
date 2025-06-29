package com.viettridao.cafe.dto.response.budget;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ExpensePageResponse {
    private List<ExpenseResponse> expenses;
    private int pageNumber;
    private int totalPages;
    private long totalElements;
}
