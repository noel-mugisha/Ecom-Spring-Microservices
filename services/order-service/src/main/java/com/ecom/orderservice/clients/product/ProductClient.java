package com.ecom.orderservice.clients.product;

import com.ecom.orderservice.clients.product.dto.ProductPurchaseResponse;
import com.ecom.orderservice.dto.request.OrderRequest;
import com.ecom.orderservice.exceptions.BusinessLogicException;
import com.ecom.orderservice.exceptions.ResourceNotFoundException;
import com.ecom.orderservice.mapper.OrderMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductClient {
    private final ProductServiceClient productServiceClient;
    private final OrderMapper orderMapper;

    public List<ProductPurchaseResponse> purchaseProducts(OrderRequest orderRequest) {
        var productPurchaseRequests = orderMapper.toProductPurchaseRequests(orderRequest.products());

        try {
            var responses = productServiceClient.purchaseProducts(productPurchaseRequests);
            if (CollectionUtils.isEmpty(responses)) {
                throw new BusinessLogicException("Product service returned empty response");
            }
            log.info("Successfully purchased {} products", responses.size());
            return responses;

        } catch (FeignException.NotFound e) {
            log.error("One or more products not found", e);
            throw new ResourceNotFoundException(
                    "One or more products in the order do not exist"
            );
        } catch (FeignException.BadRequest e) {
            log.error("Invalid product purchase request", e);
            throw new BusinessLogicException(
                    "Invalid product information. Please check product IDs and quantities."
            );
        } catch (FeignException e) {
            log.error("Product service call failed", e);
            throw new BusinessLogicException(
                    "Unable to process product purchase. Please try again later."
            );
        }
    }
}
