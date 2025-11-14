package com.ecom.productservice.mappers;

import com.ecom.productservice.dto.request.ProductRequest;
import com.ecom.productservice.dto.response.ProductPurchaseResponse;
import com.ecom.productservice.dto.response.ProductResponse;
import com.ecom.productservice.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProductMapper {
    @Mapping(target = "categoryName", source = "category.name")
    ProductResponse toProductResponse(Product productEntity);
    Product toProductEntity(ProductRequest productRequest);
    @Mapping(target = "quantityPurchased", source = "quantityPurchased")
    ProductPurchaseResponse toProductPurchaseResponse(Product productEntity, Double quantityPurchased);
}
