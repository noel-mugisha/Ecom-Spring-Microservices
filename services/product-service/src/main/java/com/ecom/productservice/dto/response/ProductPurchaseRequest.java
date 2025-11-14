package com.ecom.productservice.dto.response;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProductPurchaseRequest (
        @NotNull(message = "Product id is required")
        UUID id,
        @NotNull(message = "Quantity is required")
        Double quantity
) {}
