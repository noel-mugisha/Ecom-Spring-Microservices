package com.ecom.orderservice.clients.customer;

import com.ecom.orderservice.clients.customer.dto.CustomerClientDto;
import com.ecom.orderservice.exceptions.BusinessLogicException;
import com.ecom.orderservice.exceptions.ResourceNotFoundException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerClient {
    private final CustomerServiceClient customerServiceClient;

    public CustomerClientDto getCustomerById(String customerId) {
        try {
            return customerServiceClient.getCustomerById(customerId);
        } catch (FeignException.NotFound e) {
            log.error("Customer with id:: {} not found", customerId);
            throw new ResourceNotFoundException("Customer not found with id: " + customerId);
        } catch (FeignException e) {
            throw new BusinessLogicException("Unable to find customer. Please try again later.");
        }
    }
}
