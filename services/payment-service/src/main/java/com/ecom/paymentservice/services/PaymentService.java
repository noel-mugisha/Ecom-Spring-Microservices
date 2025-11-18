package com.ecom.paymentservice.services;

import com.ecom.paymentservice.dto.request.PaymentRequest;
import com.ecom.paymentservice.dto.response.PaymentResponse;
import com.ecom.paymentservice.exceptions.ResourceNotFoundException;
import com.ecom.paymentservice.kafka.PaymentNotification;
import com.ecom.paymentservice.kafka.PaymentProducer;
import com.ecom.paymentservice.mapper.PaymentMapper;
import com.ecom.paymentservice.repository.PaymentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final PaymentProducer paymentProducer;

    public PaymentResponse createPayment(PaymentRequest paymentRequest) {
        var savedPayment = paymentRepository.save(paymentMapper.toPaymentEntity(paymentRequest));
        // send payment event to the notification service
        var paymentNotification = new PaymentNotification(
                paymentRequest.orderReference(),
                paymentRequest.amount(),
                paymentRequest.paymentMethod(),
                paymentRequest.customer().firstName(),
                paymentRequest.customer().lastName(),
                paymentRequest.customer().email()
        );
        paymentProducer.sendPaymentNotification(paymentNotification);
        log.info("Payment notification sent successfully for order {}", paymentRequest.orderReference());
        return paymentMapper.toPaymentResponse(savedPayment);
    }

    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(paymentMapper::toPaymentResponse)
                .toList();
    }

    public PaymentResponse getPaymentById(UUID paymentId) {
        var payment = paymentRepository.findById(paymentId).orElseThrow(
                () -> new ResourceNotFoundException("Payment not found with id: " + paymentId)
        );
        return paymentMapper.toPaymentResponse(payment);
    }
}
