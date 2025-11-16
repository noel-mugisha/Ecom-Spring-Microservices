package com.ecom.orderservice.clients.customer;

import com.ecom.orderservice.dto.ApiMessageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "customer-service", path = "api/v1/customers")
public interface CustomerServiceClient {
    @GetMapping("/exists/{id}")
    ApiMessageResponse existsById(@PathVariable String id);
}
