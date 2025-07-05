package com.viettridao.cafe.dto.response.invoice;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.viettridao.cafe.common.InvoiceStatus;
import com.viettridao.cafe.dto.response.invoicedetail.InvoiceDetailResponse;
import com.viettridao.cafe.dto.response.promotion.PromotionResponse;
import com.viettridao.cafe.dto.response.reservation.ReservationResponse;

@Getter
@Setter
public class InvoiceResponse {

    private Integer id;

    private Double totalAmount;

    private LocalDateTime createdAt;

    private InvoiceStatus status;

    private Boolean deleted;

    private PromotionResponse promotion;

    private List<InvoiceDetailResponse> invoiceDetails;

    private List<ReservationResponse> reservations;

}
