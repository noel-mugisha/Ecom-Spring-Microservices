package com.ecom.productservice.repositories;

import com.ecom.productservice.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    boolean existsByName(String name);

    Optional<Product> findByName(String name);
}
