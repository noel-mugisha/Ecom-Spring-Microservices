package com.ecom.notificationservice.kafka;

import com.ecom.notificationservice.entity.Notification;
import com.ecom.notificationservice.enums.NotificationType;
import com.ecom.notificationservice.kafka.dto.OrderConfirmation;
import com.ecom.notificationservice.kafka.dto.PaymentConfirmation;
import com.ecom.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {
    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = "order-topic", groupId = "notification-service")
    public void consumeOrderTopic(OrderConfirmation orderConfirmation) {
        log.info("Consuming order confirmation from order: {}", orderConfirmation.orderReference());
        var notification = Notification.builder()
                .notificationType(NotificationType.ORDER_CONFIRMATION)
                .orderConfirmation(orderConfirmation)
                .build();
        notificationRepository.save(notification);
        // TODO :send confirmation email
    }

    @KafkaListener(topics = "payment-topic", groupId = "notification-service")
    public void consumePaymentTopic(PaymentConfirmation paymentConfirmation) {
        log.info("Consuming payment confirmation from order: {}", paymentConfirmation.orderReference());
        var notification = Notification.builder()
                .notificationType(NotificationType.PAYMENT_CONFIRMATION)
                .paymentConfirmation(paymentConfirmation)
                .build();
        notificationRepository.save(notification);
        // TODO :send confirmation email
    }
}
