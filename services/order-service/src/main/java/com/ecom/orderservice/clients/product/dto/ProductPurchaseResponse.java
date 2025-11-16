package com.ecom.orderservice.clients.product.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductPurchaseResponse(
        UUID id,
        String name,
        String description,
        Double quantityPurchased,
        BigDecimal price
) {
}
