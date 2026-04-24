package com.mauricio.sunshine.api.controller;

import com.mauricio.sunshine.api.dto.CreateProductRequest;
import com.mauricio.sunshine.api.dto.ProductResponse;
import com.mauricio.sunshine.api.dto.UpdateProductRequest;
import com.mauricio.sunshine.api.dto.UpdateProductStatusRequest;
import com.mauricio.sunshine.persistence.entity.ProductEntity;
import com.mauricio.sunshine.service.ProductService;
import com.mauricio.sunshine.service.PermissionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/products")
public class ProductController {

  private final ProductService productService;
  private final PermissionService permissionService;

  public ProductController(ProductService productService, PermissionService permissionService) {
    this.productService = productService;
    this.permissionService = permissionService;
  }

  @PostMapping
  public ProductResponse createProduct(
    @RequestHeader("X-User-Id") UUID userId,
    @PathVariable UUID restaurantId,
    @Valid @RequestBody CreateProductRequest req
  ) {

    permissionService.requireOwnerOrManager(userId, restaurantId);

    ProductEntity product = productService.createProduct(
    restaurantId,
    req.name(),
    req.price(),
    req.category()
    );
    return toResponse(product);
  }

  @GetMapping
  public List<ProductResponse> getProducts(
    @RequestHeader("X-User-Id") UUID userId,
    @PathVariable UUID restaurantId
  ) {
    permissionService.requireOwnerOrManagerOrCashier(userId, restaurantId);

    return productService.getProductsByRestaurant(restaurantId)
            .stream()
            .map(this::toResponse)
            .toList();
  }

  @GetMapping("/{productId}")
  public ProductResponse getProduct(
    @PathVariable UUID restaurantId,
    @PathVariable UUID productId
  ) {
    ProductEntity product = productService.getProduct(restaurantId, productId);

    return toResponse(product);
  }

  @PutMapping("/{productId}")
  public ProductResponse updateProduct(
    @RequestHeader("X-User-Id") UUID userId,
    @PathVariable UUID restaurantId,
    @PathVariable UUID productId,
    @Valid @RequestBody UpdateProductRequest req
  ){

    permissionService.requireOwnerOrManager(userId, restaurantId);

    ProductEntity product = productService.updateProduct(
      restaurantId,
      productId,
      req.name(),
      req.price(),
      req.category()
    );

    return toResponse(product);
  }

  @PatchMapping("/{productId}/status")
  public ProductResponse updateProcutStatus(
    @RequestHeader("X-User-Id") UUID userId,
    @PathVariable UUID restaurantId,
    @PathVariable UUID productId,
    @Valid @RequestBody UpdateProductStatusRequest req
  ){

    permissionService.requireOwnerOrManager(userId, restaurantId);

    ProductEntity product = productService.updateProducStatus(
      restaurantId,
      productId,
      req.active()
    );

    return toResponse(product);
  }

  @PatchMapping("/{productId}/deactivate")
  public ProductResponse deactivateProduct(
    @RequestHeader("X-User-Id") UUID userId,
    @PathVariable UUID restaurantId,
    @PathVariable UUID productId
  ) {

    permissionService.requireOwnerOrManager(userId, restaurantId);

    ProductEntity product = productService.deactivateProduct(
      restaurantId,
      productId
    );

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
