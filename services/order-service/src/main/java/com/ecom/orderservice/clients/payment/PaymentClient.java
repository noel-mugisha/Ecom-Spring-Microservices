package com.ecom.orderservice.clients.payment;

import com.ecom.orderservice.clients.payment.dto.PaymentClientRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentClient {
    private final PaymentServiceClient paymentServiceClient;

    public void sendPaymentRequest (PaymentClientRequest request) {
        paymentServiceClient.createPayment(request);
    }
}
