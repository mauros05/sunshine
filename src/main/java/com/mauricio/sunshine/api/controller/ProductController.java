package com.mauricio.sunshine.api.controller;

import com.mauricio.sunshine.api.dto.CreateProductRequest;
import com.mauricio.sunshine.api.dto.ProductResponse;
import com.mauricio.sunshine.persistence.entity.ProductEntity;
import com.mauricio.sunshine.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ProductResponse createProduct(
            @PathVariable UUID restaurantId,
            @Valid @RequestBody CreateProductRequest req
    ) {
       ProductEntity product = productService.createProduct(
               restaurantId,
               req.name(),
               req.price(),
               req.category()
       );
       return toResponse(product);
    }

    @GetMapping
    public List<ProductResponse> getProducts(@PathVariable UUID restaurantId) {

        return productService.getProductsByRestaurant(restaurantId)
                .stream().map(this::toResponse).toList();
    }

    @GetMapping("/{productId}")
    public ProductResponse getProduct(
            @PathVariable UUID restaurantId,
            @PathVariable UUID productId
    ) {

        ProductEntity product = productService.getProduct(restaurantId, productId);

        return toResponse(product);
    }

    @PatchMapping("/{productId}/deactivate")
    public ProductResponse deactivateProduct(
            @PathVariable UUID restaurantId,
            @PathVariable UUID productId
    ) {
        ProductEntity product = productService.deactivateProduct(restaurantId, productId);

        return toResponse(product);
    }

    private ProductResponse toResponse(ProductEntity product) {
        return new ProductResponse(
                product.getId(),
                product.getRestaurant().getId(),
                product.getName(),
                product.getPrice(),
                product.getCategory(),
                product.isActive(),
                product.getCreatedAt()
        );
    }
}
