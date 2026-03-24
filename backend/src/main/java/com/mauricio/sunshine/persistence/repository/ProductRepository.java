package com.mauricio.sunshine.persistence.repository;

import com.mauricio.sunshine.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends  JpaRepository<ProductEntity, UUID> {

    List<ProductEntity> findByRestaurantId(UUID restaurantId);

    Optional<ProductEntity> findByIdAndRestaurantId(UUID productId, UUID restaurantId);

}
