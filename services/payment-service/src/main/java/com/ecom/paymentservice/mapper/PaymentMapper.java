package com.ecom.paymentservice.mapper;

import com.ecom.paymentservice.dto.request.PaymentRequest;
import com.ecom.paymentservice.dto.response.PaymentResponse;
import com.ecom.paymentservice.entities.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface PaymentMapper {
    @Mapping(target = "paymentId", source = "id")
    PaymentResponse toPaymentResponse(Payment paymentEntity);

    @Mapping(target = "customerId", source = "customer.id")
    Payment toPaymentEntity(PaymentRequest paymentRequest);
}
