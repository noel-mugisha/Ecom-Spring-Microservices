package com.ecom.productservice.controllers;

import com.ecom.productservice.dto.request.ProductRequest;
import com.ecom.productservice.dto.request.ProductPurchaseRequest;
import com.ecom.productservice.dto.response.ProductPurchaseResponse;
import com.ecom.productservice.dto.response.ProductResponse;
import com.ecom.productservice.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> addProduct (
            @RequestBody @Valid ProductRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        ProductResponse product = productService.createOrUpdateProduct(request);
        var uri = uriBuilder.path("/products/{id}").buildAndExpand(product.id()).toUri();
        return ResponseEntity.created(uri).body(product);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById (@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping("/purchase")
    public ResponseEntity<List<ProductPurchaseResponse>> purchaseProducts (
            @RequestBody @Valid List<ProductPurchaseRequest> productPurchaseRequests
    ) {
        return ResponseEntity.ok(productService.purchaseProducts(productPurchaseRequests));
    }

}
