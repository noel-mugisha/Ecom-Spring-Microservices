package com.ecom.paymentservice.controllers;

import com.ecom.paymentservice.dto.ApiMessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<ApiMessageResponse> healthCheck() {
        return ResponseEntity.ok(new ApiMessageResponse("Payment Service is up and running."));
    }
}
