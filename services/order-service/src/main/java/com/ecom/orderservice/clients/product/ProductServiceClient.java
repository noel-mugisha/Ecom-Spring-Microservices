package com.ecom.orderservice.clients.product;

import com.ecom.orderservice.clients.product.dto.ProductPurchaseRequest;
import com.ecom.orderservice.clients.product.dto.ProductPurchaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service", path = "api/v1/products")
public interface ProductServiceClient {
    @PostMapping("/purchase")
    List<ProductPurchaseResponse> purchaseProducts(
            @RequestBody List<ProductPurchaseRequest> productPurchaseRequests
    );
}
