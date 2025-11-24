package com.ecom.customerservice.services;

import com.ecom.customerservice.dto.CustomerDto;
import com.ecom.customerservice.exceptions.DuplicateEmailException;
import com.ecom.customerservice.exceptions.ResourceNotFoundException;
import com.ecom.customerservice.mappers.CustomerMapper;
import com.ecom.customerservice.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    private boolean checkDuplicateEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Caching(evict = {
            @CacheEvict(value = "customers.all", allEntries = true),
            @CacheEvict(value = "customers.byId", allEntries = true)
    })
    public CustomerDto addCustomer(CustomerDto requestDto) {
        if (checkDuplicateEmail(requestDto.email()))
            throw new DuplicateEmailException("Email already exists");
        var savedCustomer = customerRepository.save(customerMapper.toEntity(requestDto));
        return customerMapper.toDto(savedCustomer);
    }

    @Caching(evict = {
            @CacheEvict(value = "customers.byId", key = "#id"),
            @CacheEvict(value = "customers.all", allEntries = true)
    })
    public CustomerDto updateCustomer(String id, CustomerDto requestDto) {
        if (customerRepository.findById(id).isEmpty())
            throw new ResourceNotFoundException("Customer not found");
        var customer = customerRepository.findById(id).get();
        customerMapper.updateCustomer(customer, requestDto);
        return customerMapper.toDto(customerRepository.save(customer));
    }

    @Cacheable(value = "customers.byId", key = "#id")
    public CustomerDto getCustomerById(String id) {
        return customerRepository.findById(id)
                .map(customerMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }

    @Caching(evict = {
            @CacheEvict(value = "customers.byId", key = "#id"),
            @CacheEvict(value = "customers.all", allEntries = true)
    })
    public void deleteCustomerById(String id) {
        if (customerRepository.findById(id).isEmpty())
            throw new ResourceNotFoundException("Customer not found");
        customerRepository.deleteById(id);
    }

    @Cacheable(value = "customers.all")
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::toDto)
                .toList();
    }

    @Cacheable(value = "customers.exists", key = "#id")
    public boolean existsById(String id) {
        return customerRepository.existsById(id);
    }
}
