package com.ecom.orderservice.controllers;

import com.ecom.orderservice.dto.request.OrderRequest;
import com.ecom.orderservice.dto.response.OrderResponse;
import com.ecom.orderservice.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody @Valid OrderRequest orderRequest,
            UriComponentsBuilder uriBuilder
    ) {
        OrderResponse orderResponse = orderService.createOrder(orderRequest);
        var uri = uriBuilder.path("/orders/{id}").buildAndExpand(orderResponse.orderId()).toUri();
        return ResponseEntity.created(uri).body(orderResponse);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable UUID id) {
        orderService.deleteOrderById(id);
        return ResponseEntity.noContent().build();
    }
}
