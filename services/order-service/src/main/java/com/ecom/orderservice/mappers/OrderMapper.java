package com.ecom.orderservice.mappers;

import com.ecom.orderservice.clients.product.dto.ProductPurchaseRequest;
import com.ecom.orderservice.dto.request.OrderRequest;
import com.ecom.orderservice.dto.request.PurchaseRequest;
import com.ecom.orderservice.dto.response.OrderResponse;
import com.ecom.orderservice.entities.Order;
import com.ecom.orderservice.enums.PaymentMethod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OrderMapper {
    List<ProductPurchaseRequest> toProductPurchaseRequests(List<PurchaseRequest> purchaseRequests);

    Order toOrderEntity(OrderRequest orderRequest, String reference, PaymentMethod paymentMethod);

    @Mapping(target = "orderId", source = "id")
    OrderResponse toOrderResponse(Order order);
}
