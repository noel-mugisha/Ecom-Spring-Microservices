package com.ecom.customerservice.controllers;

import com.ecom.customerservice.dto.ApiMessageResponse;
import com.ecom.customerservice.dto.CustomerDto;
import com.ecom.customerservice.services.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer (
            @RequestBody @Valid CustomerDto requestDto,
            UriComponentsBuilder uriBuilder
    ) {
        CustomerDto createdCustomer = customerService.addCustomer(requestDto);
        var uri = uriBuilder.path("/customers/{id}")
                .buildAndExpand(createdCustomer.id())
                .toUri();
        return ResponseEntity.created(uri).body(createdCustomer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer (
            @PathVariable String id,
            @RequestBody @Valid CustomerDto requestDto
    ) {
        var customer = customerService.updateCustomer(id, requestDto);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById (@PathVariable String id) {
        var customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers () {
        var customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerById (@PathVariable String id) {
        customerService.deleteCustomerById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<ApiMessageResponse> existsById (@PathVariable String id) {
        String response = String.valueOf(customerService.existsById(id));
        return ResponseEntity.ok(new ApiMessageResponse(response));
    }
}
