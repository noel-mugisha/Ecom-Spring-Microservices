package com.ecom.customerservice.controllers;

import com.ecom.customerservice.dto.ApiMessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<ApiMessageResponse> getHealth() {
        return ResponseEntity.ok(new ApiMessageResponse("Customer service is up and running"));
    }
}
