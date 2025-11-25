package com.ecom.orderservice.clients.product;

import com.ecom.orderservice.clients.product.dto.ProductPurchaseRequest;
import com.ecom.orderservice.clients.product.dto.ProductPurchaseResponse;
import com.ecom.orderservice.exceptions.BusinessLogicException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductClientFallback implements ProductServiceClient{
    @Override
    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> productPurchaseRequests) {
        throw new BusinessLogicException("An error occurred while processing the products purchase: Product service is unavailable.");
    }
}
