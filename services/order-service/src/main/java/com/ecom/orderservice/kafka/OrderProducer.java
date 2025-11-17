package com.ecom.orderservice.kafka;

import com.ecom.orderservice.kafka.events.OrderConfirmationEvent;
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
public class OrderProducer {
    private final KafkaTemplate<String, OrderConfirmationEvent> kafkaTemplate;

    public void sendOrderConfirmationEvent(OrderConfirmationEvent event) {
        log.info("Producing order confirmation event for order id: {}", event.orderReference());
        Message<OrderConfirmationEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, "order-topic")
                .setHeader(KafkaHeaders.KEY, event.orderReference())
                .setHeader("producer", "order-service")
                .build();
        kafkaTemplate.send(message);
    }
}
