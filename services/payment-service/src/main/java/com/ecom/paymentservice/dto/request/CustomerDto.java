package com.ecom.paymentservice.dto.request;

import jakarta.validation.constraints.NotNull;

public record CustomerDto(
        @NotNull(message = "Customer ID is required")
        String id,
        @NotNull(message = "First name is required")
        String firstName,
        @NotNull(message = "Last name is required")
        String lastName,
        @NotNull(message = "Email is required")
        String email
) {
}
