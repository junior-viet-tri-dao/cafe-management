package com.viettridao.cafe.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageResponse {
    // Số trang bao nhiêu
    private long pageNumber;
    // số lượng phần tử trong trang
    private long pageSize;
    // tổng sô trang trong tất cả trang
    private long totalPages;
    // tông số phần tử
    private long totalElements;
}
