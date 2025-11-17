package com.ecom.orderservice.services;

import com.ecom.orderservice.clients.customer.CustomerClient;
import com.ecom.orderservice.clients.customer.dto.CustomerClientDto;
import com.ecom.orderservice.clients.product.ProductClient;
import com.ecom.orderservice.clients.product.dto.ProductPurchaseResponse;
import com.ecom.orderservice.dto.OrderLineDto;
import com.ecom.orderservice.dto.request.OrderRequest;
import com.ecom.orderservice.dto.response.OrderResponse;
import com.ecom.orderservice.entities.Order;
import com.ecom.orderservice.entities.OrderLine;
import com.ecom.orderservice.exceptions.ResourceNotFoundException;
import com.ecom.orderservice.kafka.OrderProducer;
import com.ecom.orderservice.kafka.events.OrderConfirmationEvent;
import com.ecom.orderservice.mappers.OrderLineMapper;
import com.ecom.orderservice.mappers.OrderMapper;
import com.ecom.orderservice.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    @Autowired
    @Lazy
    private OrderService self;
    private final OrderRepository orderRepository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderMapper orderMapper;
    private final OrderProducer orderProducer;
    private final OrderLineMapper orderLineMapper;

    public OrderResponse createOrder(OrderRequest orderRequest) {
        log.info("Creating order for customer: {}", orderRequest.customerId());
        // Step 1: get the customer details (external call)
        var customer = customerClient.getCustomerById(orderRequest.customerId());
        // Step 2: Purchase products (external call)
        var productPurchaseResponses = productClient.purchaseProducts(orderRequest);
        // Step 3: Calculate the total amount
        var totalAmount = calculateTotalAmount(productPurchaseResponses);
        // Step 4: Persist the order
        var order = self.persistOrder(orderRequest, productPurchaseResponses, totalAmount);
        // TODO: Step5: Start payment process

        // Step6: Send order confirmation to the notification microservice
        sendOrderConfirmationEvent(order, customer, productPurchaseResponses);

        return orderMapper.toOrderResponse(order);
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

    public List<OrderLineDto> getOrderLinesByOrderId(UUID orderId) {
        var order = orderRepository.findById(orderId).orElseThrow(
                () -> new ResourceNotFoundException("Order not found with orderId: " + orderId)
        );
        var orderLines = new ArrayList<>(order.getOrderLines());
        return orderLineMapper.toOrderLineDtos(orderLines);
    }

    /**
     * Sends order confirmation event to notification microservice.
     */
    private void sendOrderConfirmationEvent(Order order, CustomerClientDto customer, List<ProductPurchaseResponse> products) {
        var event = new OrderConfirmationEvent(
                order.getReference(),
                order.getTotalAmount(),
                order.getPaymentMethod(),
                customer,
                products
        );
        orderProducer.sendOrderConfirmationEvent(event);
        log.info("Order confirmation event sent successfully for order {}", order.getReference());
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
    protected Order persistOrder(
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
        return savedOrder;
    }

    /**
     * Generates a unique order reference.
     */
    private String generateOrderReference() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}