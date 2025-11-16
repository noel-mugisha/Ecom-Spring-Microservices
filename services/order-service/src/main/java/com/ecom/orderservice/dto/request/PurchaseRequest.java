package com.ecom.orderservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record PurchaseRequest (
        @NotNull(message = "Product orderId must be provided")
        UUID productId,
        @NotNull(message = "Quantity must be provided")
        @Positive(message = "Quantity must be positive")
        Double quantity
) {}
