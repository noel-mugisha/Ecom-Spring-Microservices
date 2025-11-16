package com.ecom.orderservice.services;

import com.ecom.orderservice.clients.customer.CustomerServiceClient;
import com.ecom.orderservice.clients.product.ProductServiceClient;
import com.ecom.orderservice.clients.product.dto.ProductPurchaseResponse;
import com.ecom.orderservice.dto.request.OrderRequest;
import com.ecom.orderservice.dto.response.OrderResponse;
import com.ecom.orderservice.entities.Order;
import com.ecom.orderservice.entities.OrderLine;
import com.ecom.orderservice.exceptions.BusinessLogicException;
import com.ecom.orderservice.exceptions.ResourceNotFoundException;
import com.ecom.orderservice.mapper.OrderMapper;
import com.ecom.orderservice.repositories.OrderRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerServiceClient customerServiceClient;
    private final ProductServiceClient productServiceClient;
    private final OrderMapper orderMapper;

    public OrderResponse createOrder(OrderRequest orderRequest) {
        log.info("Creating order for customer: {}", orderRequest.customerId());
        // Step 1: Validate customer exists (external call)
        validateCustomerExists(orderRequest.customerId());

        // Step 2: Purchase products (external call)
        var productPurchaseResponses = purchaseProducts(orderRequest);

        // Step 3: Calculate the total amount
        var totalAmount = calculateTotalAmount(productPurchaseResponses);

        // TODO: Step4: Start payment process

        // TODO: Step5: Send order confirmation to notification microservice

        // Step 6: Persist order and order lines (single transaction)
        return persistOrder(orderRequest, productPurchaseResponses, totalAmount);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    public OrderResponse getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return orderMapper.toOrderResponse(order);
    }

    @Transactional
    public void deleteOrderById(UUID orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new ResourceNotFoundException("Order not found with orderId: " + orderId);
        }
        orderRepository.deleteById(orderId);
    }

    /**
     * Validates that the customer exists in the customer service.
     */
    private void validateCustomerExists(String customerId) {
        try {
            var apiResponse = customerServiceClient.existsById(customerId);

            if (apiResponse == null || "false".equalsIgnoreCase(apiResponse.message())) {
                log.warn("Customer not found: {}", customerId);
                throw new ResourceNotFoundException(
                        String.format("Customer with ID %s does not exist", customerId)
                );
            }
        } catch (FeignException e) {
            log.error("Customer service call failed for customer: {}", customerId, e);
            throw new BusinessLogicException(
                    "Unable to validate customer. Please try again later."
            );
        }
    }

    /**
     * Purchases products through the product service.
     */
    private List<ProductPurchaseResponse> purchaseProducts(OrderRequest orderRequest) {
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

    /**
     * Calculates the total order amount from purchased products.
     */
    private BigDecimal calculateTotalAmount(List<ProductPurchaseResponse> productResponses) {
        return productResponses.stream()
                .map(response -> response.price()
                        .multiply(BigDecimal.valueOf(response.quantityPurchased())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Persists the order and order lines in a single transaction.
     */
    @Transactional
    protected OrderResponse persistOrder(
            OrderRequest orderRequest,
            List<ProductPurchaseResponse> productResponses,
            BigDecimal totalAmount) {

        var reference = generateOrderReference();

        var orderEntity = orderMapper.toOrderEntity(
                orderRequest,
                reference,
                orderRequest.paymentMethod()
        );
        orderEntity.setTotalAmount(totalAmount);

        // Create order lines with actual purchased product data
        Set<OrderLine> orderLines = productResponses.stream()
                .map(response -> OrderLine.builder()
                        .productId(response.id())
                        .quantity(response.quantityPurchased())
                        .order(orderEntity)
                        .build())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        // Set bidirectional relationship
        orderEntity.setOrderLines(orderLines);

        // Save (cascade should handle order lines if configured)
        var savedOrder = orderRepository.save(orderEntity);

        log.info("Order created successfully: {}", savedOrder.getReference());
        return orderMapper.toOrderResponse(savedOrder);
    }

    /**
     * Generates a unique order reference.
     */
    private String generateOrderReference() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}