package com.viettridao.cafe.dto.commodity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommodityExportDTO {
    private Integer maHangHoa;

    private Integer soLuong;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate ngayXuat;
}
