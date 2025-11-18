package com.ecom.orderservice.clients.payment.dto;

import com.ecom.orderservice.clients.customer.dto.CustomerClientDto;
import com.ecom.orderservice.enums.PaymentMethod;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentClientRequest(
        UUID orderId,
        String orderReference,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        CustomerClientDto customer
) {
}
