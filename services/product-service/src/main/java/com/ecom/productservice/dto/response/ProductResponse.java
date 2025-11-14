package com.ecom.productservice.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse (
        UUID id,
        String name,
        String description,
        Double availableQuantity,
        BigDecimal price,
        String categoryName
) {}
