package com.viettridao.cafe.dto.request.reservation;

import com.viettridao.cafe.model.ReservationKey;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.*;

@Getter
@Setter
public class ReservationCreateRequest {

    private Integer tableId;

    private String customerName;

    private String customerPhone;

    private LocalDateTime reservationDate;

    private Boolean deleted = false;
}
