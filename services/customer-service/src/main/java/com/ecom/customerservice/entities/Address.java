package com.ecom.customerservice.entities;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Validated
public class Address {
    @NotBlank(message = "Street cannot be null or empty")
    private String street;
    @NotBlank(message = "House Number cannot be null or empty")
    private String houseNumber;
    @NotBlank(message = "Zipcode cannot be null or empty")
    private String zipCode;
}
