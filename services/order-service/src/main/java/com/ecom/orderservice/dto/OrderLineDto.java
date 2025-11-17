package com.ecom.orderservice.dto;

import java.util.UUID;

public record OrderLineDto(
        UUID orderLineId,
        UUID productId,
        Double quantity,
        UUID orderId
) {
}
