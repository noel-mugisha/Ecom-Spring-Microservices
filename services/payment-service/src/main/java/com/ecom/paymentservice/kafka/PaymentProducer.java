package com.ecom.paymentservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentProducer {
    private final KafkaTemplate<String, PaymentNotification> kafkaTemplate;

    public void sendPaymentNotification(PaymentNotification paymentNotification) {
        log.info("Sending payment notification for order : {}", paymentNotification.orderReference());
        Message<PaymentNotification> message = MessageBuilder
                .withPayload(paymentNotification)
                .setHeader(KafkaHeaders.TOPIC, "payment-topic")
                .setHeader(KafkaHeaders.KEY, paymentNotification.orderReference())
                .setHeader("producer", "payment-service")
                .build();
        kafkaTemplate.send(message);
    }
}
