package com.ecom.notificationservice.kafka;

import com.ecom.notificationservice.email.EmailService;
import com.ecom.notificationservice.entity.Notification;
import com.ecom.notificationservice.enums.NotificationType;
import com.ecom.notificationservice.kafka.dto.OrderConfirmation;
import com.ecom.notificationservice.kafka.dto.PaymentConfirmation;
import com.ecom.notificationservice.repository.NotificationRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    @KafkaListener(topics = "order-topic", groupId = "notification-service")
    public void consumeOrderTopic(OrderConfirmation orderConfirmation) throws MessagingException {
        log.info("Consuming order confirmation from order: {}", orderConfirmation.orderReference());
        var notification = Notification.builder()
                .notificationType(NotificationType.ORDER_CONFIRMATION)
                .orderConfirmation(orderConfirmation)
                .build();
        notificationRepository.save(notification);
        // send confirmation email
        var customerName = orderConfirmation.customer().firstName() + " " + orderConfirmation.customer().lastName();
        emailService.sendOrderConfirmationEmail(
                orderConfirmation.customer().email(),
                customerName,
                orderConfirmation.totalAmount(),
                orderConfirmation.orderReference(),
                orderConfirmation.products()
        );
    }

    @KafkaListener(topics = "payment-topic", groupId = "notification-service")
    public void consumePaymentTopic(PaymentConfirmation paymentConfirmation) throws MessagingException {
        log.info("Consuming payment confirmation from order: {}", paymentConfirmation.orderReference());
        var notification = Notification.builder()
                .notificationType(NotificationType.PAYMENT_CONFIRMATION)
                .paymentConfirmation(paymentConfirmation)
                .build();
        notificationRepository.save(notification);
        // send confirmation email
        var customerName = paymentConfirmation.customerFirstName() + " " + paymentConfirmation.customerLastName();
        emailService.sendPaymentSuccessEmail(
                paymentConfirmation.customerEmail(),
                customerName,
                paymentConfirmation.amount(),
                paymentConfirmation.orderReference()
        );
    }
}
