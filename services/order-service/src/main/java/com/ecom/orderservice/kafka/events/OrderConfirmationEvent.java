package com.ecom.orderservice.kafka.events;

import com.ecom.orderservice.clients.customer.dto.CustomerClientDto;
import com.ecom.orderservice.clients.product.dto.ProductPurchaseResponse;
import com.ecom.orderservice.enums.PaymentMethod;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmationEvent(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerClientDto customer,
        List<ProductPurchaseResponse> products
) {
}
