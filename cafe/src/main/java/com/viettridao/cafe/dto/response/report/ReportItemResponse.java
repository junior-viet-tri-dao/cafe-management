
package com.viettridao.cafe.dto.response.report;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportItemResponse {
    private LocalDate ngay;
    private long thu;
    private long chi;
}
