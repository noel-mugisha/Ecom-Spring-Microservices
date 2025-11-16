package com.ecom.orderservice.clients.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record ProductPurchaseRequest(
        @JsonProperty("id")
        UUID productId,
        Double quantity
) {
}
