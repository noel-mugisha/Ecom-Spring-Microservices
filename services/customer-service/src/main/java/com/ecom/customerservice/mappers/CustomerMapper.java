package com.ecom.customerservice.mappers;

import com.ecom.customerservice.dto.CustomerDto;
import com.ecom.customerservice.entities.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CustomerMapper {
    CustomerDto toDto(Customer customerEntity);

    Customer toEntity(CustomerDto customerDto);

    void updateCustomer(@MappingTarget Customer customer, CustomerDto customerDto);
}
