package com.ecom.orderservice.controllers;

import com.ecom.orderservice.dto.ApiMessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<ApiMessageResponse> healthCheck() {
        return ResponseEntity.ok(new ApiMessageResponse("Order Service is up and running."));
    }
}
