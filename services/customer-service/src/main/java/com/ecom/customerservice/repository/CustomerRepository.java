package com.ecom.customerservice.repository;

import com.ecom.customerservice.entities.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    boolean existsByEmail(String email);
}
