package com.ecom.notificationservice.kafka.dto;

import java.math.BigDecimal;

public record ProductDto(
        String name,
        String description,
        Double quantityPurchased,
        BigDecimal price
) {
}
