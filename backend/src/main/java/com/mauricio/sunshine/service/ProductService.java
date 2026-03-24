package com.mauricio.sunshine.service;

import com.mauricio.sunshine.persistence.entity.ProductEntity;
import com.mauricio.sunshine.persistence.entity.RestaurantEntity;
import com.mauricio.sunshine.persistence.repository.ProductRepository;
import com.mauricio.sunshine.persistence.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepo;
    private final RestaurantRepository restaurantRepo;

    public ProductService(ProductRepository productRepo, RestaurantRepository restaurantRepo){
        this.productRepo = productRepo;
        this.restaurantRepo = restaurantRepo;
    }

    public ProductEntity createProduct(UUID restaurantId, String name, BigDecimal price, String category) {
        RestaurantEntity restaurant = restaurantRepo.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante no encontrado"));

        ProductEntity product = new ProductEntity(restaurant, name, price, category);
        return productRepo.save(product);
    }

    public List<ProductEntity> getProductsByRestaurant(UUID restaurantId){
        if (!restaurantRepo.existsById(restaurantId)) {
            throw new IllegalArgumentException("Restaurante no econtrado");
        }

        return productRepo.findByRestaurantId(restaurantId);
    }

    public ProductEntity getProduct(UUID restaurantId, UUID productId) {
        return productRepo.findByIdAndRestaurantId(productId, restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));
    }

    public ProductEntity deactivateProduct(UUID restaurantId, UUID productId) {
        ProductEntity product = productRepo.findByIdAndRestaurantId(productId, restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        product.setActive(false);

        return productRepo.save(product);
    }

}
