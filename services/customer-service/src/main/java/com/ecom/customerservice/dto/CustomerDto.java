package com.ecom.customerservice.dto;

import com.ecom.customerservice.entities.Address;
import com.ecom.customerservice.validation.LowerCase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

    public record CustomerDto (
            String id,
            @NotBlank(message = "First name cannot be null or empty")
            String firstName,
            @NotBlank(message = "Last name cannot be null or empty")
            String lastName,
            @NotBlank(message = "Email cannot be null or empty")
            @Email(message = "Email is not valid")
            @LowerCase(message = "Email should be in lowercase")
            String email,
            Address address
    ) {}
