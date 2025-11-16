package com.ecom.orderservice.dto.request;

import com.ecom.orderservice.enums.PaymentMethod;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderRequest(
        @NotNull(message = "Customer orderId must be provided")
        String customerId,
        @NotEmpty(message = "At least one product must be included in the order")
        List<PurchaseRequest> products,
        @NotNull(message = "Payment method must be provided")
        PaymentMethod paymentMethod
) {
}
