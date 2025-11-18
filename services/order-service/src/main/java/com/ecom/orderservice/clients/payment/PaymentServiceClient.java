package com.ecom.orderservice.clients.payment;

import com.ecom.orderservice.clients.payment.dto.PaymentClientRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service", path = "api/v1/payments")
public interface PaymentServiceClient {

    @PostMapping
    Object createPayment(@RequestBody PaymentClientRequest request);
}
