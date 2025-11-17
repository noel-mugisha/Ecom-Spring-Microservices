package com.ecom.orderservice.clients.customer;

import com.ecom.orderservice.clients.customer.dto.CustomerClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service", path = "api/v1/customers")
public interface CustomerServiceClient {
    @GetMapping("/{id}")
    CustomerClientDto getCustomerById(@PathVariable String id);
}
