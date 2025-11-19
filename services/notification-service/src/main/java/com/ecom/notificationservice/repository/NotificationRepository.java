package com.ecom.notificationservice.repository;

import com.ecom.notificationservice.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
}
