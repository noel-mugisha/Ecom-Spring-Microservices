package com.ecom.orderservice.mappers;

import com.ecom.orderservice.dto.OrderLineDto;
import com.ecom.orderservice.entities.OrderLine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE
)
public interface OrderLineMapper {
    @Mapping(target = "orderLineId", source = "id")
    @Mapping(target = "orderId", source = "order.id")
    OrderLineDto toOrderLineDto(OrderLine orderLine);

    List<OrderLineDto> toOrderLineDtos(List<OrderLine> orderLines);
}
