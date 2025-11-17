package com.ecom.orderservice.clients.customer.dto;

public record CustomerClientDto(
        String id,
        String firstName,
        String lastName,
        String email
) {
}
