package com.ecom.productservice.services;

import com.ecom.productservice.dto.request.ProductRequest;
import com.ecom.productservice.dto.request.ProductPurchaseRequest;
import com.ecom.productservice.dto.response.ProductPurchaseResponse;
import com.ecom.productservice.dto.response.ProductResponse;
import com.ecom.productservice.entities.Category;
import com.ecom.productservice.entities.Product;
import com.ecom.productservice.exceptions.InsufficientQuantityException;
import com.ecom.productservice.exceptions.ResourceNotFoundException;
import com.ecom.productservice.mappers.ProductMapper;
import com.ecom.productservice.repositories.CategoryRepository;
import com.ecom.productservice.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "products.all", allEntries = true),
            @CacheEvict(value = "products.byId", allEntries = true)
    })
    public ProductResponse createOrUpdateProduct(ProductRequest request) {
        Optional<Product> existingProduct = productRepository.findByName(request.name());
        if (existingProduct.isPresent()) {
            // Path 1: Product exists, update its quantity.
            Product updatedProduct = updateExistingProductQuantity(existingProduct.get(), request);
            return productMapper.toProductResponse(updatedProduct);
        } else {
            // Path 2: Product does not exist, create a new one.
            Product newProduct = createNewProduct(request);
            return productMapper.toProductResponse(newProduct);
        }
    }

    @Cacheable(value = "products.all")
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    @Cacheable(value = "products.byId", key = "#id")
    public ProductResponse getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id [" + id + "] not found."));
        return productMapper.toProductResponse(product);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "products.all", allEntries = true),
            @CacheEvict(value = "products.byId", allEntries = true)
    })
    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> purchaseRequests) {
        return purchaseRequests.stream().map(request -> {
            Product product = productRepository.findById(request.id())
                    .orElseThrow(() -> new ResourceNotFoundException("Product with id [" + request.id() + "] not found."));
            if (product.getAvailableQuantity() < request.quantity()) {
                throw new InsufficientQuantityException("Insufficient quantity for product with id [" + request.id() + "]. Requested: " + request.quantity() + ", Available: " + product.getAvailableQuantity());
            }
            // Deduct the purchased quantity
            double newQuantity = product.getAvailableQuantity() - request.quantity();
            product.setAvailableQuantity(newQuantity);
            Product updatedProduct = productRepository.save(product);
            return productMapper.toProductPurchaseResponse(updatedProduct, request.quantity());
        }).toList();
    }

    /**
     * Helper Methods
     */
    private Product updateExistingProductQuantity(Product existingProduct, ProductRequest request) {
        var newQuantity = existingProduct.getAvailableQuantity() + request.availableQuantity();
        existingProduct.setAvailableQuantity(newQuantity);
        return productRepository.save(existingProduct);
    }
    private Product createNewProduct(ProductRequest request) {
        Category productCategory = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Cannot create product. Category with id [" + request.categoryId() + "] not found."));
        Product newProduct = productMapper.toProductEntity(request);
        newProduct.setCategory(productCategory);
        return productRepository.save(newProduct);
    }

}