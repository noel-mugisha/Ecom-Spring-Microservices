package com.ecom.productservice.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductRequest (
        @NotBlank(message = "Product name cannot be blank")
        String name,
        String description,
        @NotNull(message = "Available quantity is required")
        @Positive(message = "Available quantity must be greater than zero")
        Double availableQuantity,
        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        @Digits(integer = 10, fraction = 2, message = "Price format is invalid")
        BigDecimal price,
        @NotNull(message = "Category id is required")
        UUID categoryId
) {}
