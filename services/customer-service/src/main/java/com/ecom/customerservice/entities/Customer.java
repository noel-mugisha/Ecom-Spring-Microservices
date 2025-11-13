package com.ecom.customerservice.entities;

import jakarta.validation.Valid;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "customers")
public class Customer {
    @Id
    private String id;
    @Field("first_name")
    private String firstName;
    @Field("last_name")
    private String lastName;
    @Indexed(unique = true)
    private String email;
    @Valid
    private Address address;
}
