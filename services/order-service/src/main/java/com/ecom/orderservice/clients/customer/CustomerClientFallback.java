package com.ecom.orderservice.clients.customer;

import com.ecom.orderservice.clients.customer.dto.CustomerClientDto;
import com.ecom.orderservice.exceptions.BusinessLogicException;
import org.springframework.stereotype.Component;

@Component
public class CustomerClientFallback implements CustomerServiceClient{
    @Override
    public CustomerClientDto getCustomerById(String id) {
        throw new BusinessLogicException("Customer service is currently unavailable. Please try again later.");
    }
}
