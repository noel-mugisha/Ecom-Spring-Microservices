package com.ecom.paymentservice.controllers;

import com.ecom.paymentservice.dto.request.PaymentRequest;
import com.ecom.paymentservice.dto.response.PaymentResponse;
import com.ecom.paymentservice.services.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @RequestBody @Valid PaymentRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        PaymentResponse response = paymentService.createPayment(request);
        var uri = uriBuilder.path("/payments/{id}").buildAndExpand(response.paymentId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getSinglePayment(
            @PathVariable UUID paymentId
    ) {
        return ResponseEntity.ok(paymentService.getPaymentById(paymentId));
    }
}
