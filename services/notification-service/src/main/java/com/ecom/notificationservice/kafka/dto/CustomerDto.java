package com.ecom.notificationservice.kafka.dto;

public record CustomerDto (
        String firstName,
        String lastName,
        String email
) {
}
