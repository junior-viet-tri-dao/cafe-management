package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.Pay.PaymentRequest;
import com.viettridao.cafe.dto.response.Pay.PaymentResponse;

public interface PaymentService {
    PaymentResponse processPayment(PaymentRequest request);
}

