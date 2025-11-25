package com.ecom.orderservice.clients.payment;

import com.ecom.orderservice.clients.payment.dto.PaymentClientRequest;
import com.ecom.orderservice.exceptions.BusinessLogicException;
import org.springframework.stereotype.Component;

@Component
public class PaymentClientFallback implements PaymentServiceClient{
    @Override
    public Object createPayment(PaymentClientRequest request) {
        throw new BusinessLogicException("Error while processing the payment request: Payment service is unavailable.");
    }
}
