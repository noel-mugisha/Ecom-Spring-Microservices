package com.ecom.paymentservice.dto.request;

import com.ecom.paymentservice.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequest (
        @NotNull(message = "Order ID is required")
        UUID orderId,
        @NotBlank(message = "Order reference must not be blank")
        String orderReference,
        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than zero")
        BigDecimal amount,
        @NotNull(message = "Payment method is required")
        PaymentMethod paymentMethod,
        @Valid
        CustomerDto customer
) {
}
