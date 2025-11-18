package com.ecom.paymentservice.controllers;

import com.ecom.paymentservice.dto.ApiMessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    public ResponseEntity<ApiMessageResponse> healthCheck() {
        return ResponseEntity.ok(new ApiMessageResponse("Payment Service is up and running."));
    }
}
