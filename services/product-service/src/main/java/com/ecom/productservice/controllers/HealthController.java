package com.ecom.productservice.controllers;

import com.ecom.productservice.dto.ApiMessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<ApiMessageResponse> getHealth() {
        return ResponseEntity.ok(new ApiMessageResponse("Product Service is up and running."));
    }
}
