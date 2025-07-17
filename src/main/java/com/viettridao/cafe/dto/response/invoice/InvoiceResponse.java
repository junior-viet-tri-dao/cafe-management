package com.viettridao.cafe.dto.response.invoice;

import java.time.LocalDateTime;
import java.util.List;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.dto.response.promotion.PromotionResponse;
import com.viettridao.cafe.dto.response.reservation.ReservationResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceResponse {
    private Integer id;
    private Double totalAmount;
    private LocalDateTime createdAt;
    private InvoiceStatus status;
    private Boolean isDeleted;
    private PromotionResponse promotion;
    private ReservationResponse reservations;
    private List<InvoiceDetailResponse> invoiceDetails;
}
