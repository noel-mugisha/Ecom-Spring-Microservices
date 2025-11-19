package com.ecom.notificationservice.entity;

import com.ecom.notificationservice.enums.NotificationType;
import com.ecom.notificationservice.kafka.dto.OrderConfirmation;
import com.ecom.notificationservice.kafka.dto.PaymentConfirmation;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Document(collection = "notifications")
public class Notification {
    @Id
    private String notificationId;
    @Indexed
    private NotificationType notificationType;
    @CreatedDate
    private LocalDateTime createdAt;
    private OrderConfirmation orderConfirmation;
    private PaymentConfirmation paymentConfirmation;
}
