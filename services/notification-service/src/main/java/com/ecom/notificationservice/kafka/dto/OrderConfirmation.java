package com.ecom.notificationservice.kafka.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        String paymentMethod,
        CustomerDto customer,
        List<ProductDto> products
        ) {
}
